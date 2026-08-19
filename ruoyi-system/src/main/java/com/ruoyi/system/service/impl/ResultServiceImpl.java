package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.Event;
import com.ruoyi.system.domain.Registration;
import com.ruoyi.system.domain.Result;
import com.ruoyi.system.domain.vo.PassArrivalVo;
import com.ruoyi.system.domain.vo.SegmentVo;
import com.ruoyi.system.mapper.EventMapper;
import com.ruoyi.system.mapper.PassRecordMapper;
import com.ruoyi.system.mapper.RegistrationMapper;
import com.ruoyi.system.mapper.ResultMapper;
import com.ruoyi.system.service.IResultService;

/**
 * 成绩管理 服务层实现
 *
 * @author ruoyi
 */
@Service("resultService")
public class ResultServiceImpl implements IResultService
{
    private static final Logger log = LoggerFactory.getLogger(ResultServiceImpl.class);

    @Autowired
    private ResultMapper resultMapper;

    @Autowired
    private PassRecordMapper passRecordMapper;

    @Autowired
    private RegistrationMapper registrationMapper;

    @Autowired
    private EventMapper eventMapper;

    /**
     * 查询成绩列表
     *
     * @param result 成绩信息
     * @return 成绩集合
     */
    @Override
    public List<Result> selectResultList(Result result)
    {
        return resultMapper.selectResultList(result);
    }

    /**
     * 查询成绩信息
     *
     * @param id 成绩ID
     * @return 成绩信息
     */
    @Override
    public Result selectResultById(Long id)
    {
        return resultMapper.selectResultById(id);
    }

    /**
     * 查询我的成绩列表
     *
     * @param personId 参赛人员ID
     * @return 成绩集合
     */
    @Override
    public List<Result> selectMyResults(Long personId)
    {
        return resultMapper.selectMyResults(personId);
    }

    /**
     * 新增成绩（同一赛事同一选手仅允许一条成绩）
     *
     * @param result 成绩信息
     * @return 结果
     */
    @Override
    public int insertResult(Result result)
    {
        Result exist = resultMapper.selectByEventAndPerson(result.getEventId(), result.getPersonId());
        if (StringUtils.isNotNull(exist))
        {
            throw new ServiceException("该选手成绩已存在");
        }
        result.setCreateTime(DateUtils.getNowDate());
        if (StringUtils.isNull(result.getStatus()))
        {
            result.setStatus(0); // 0未完赛
        }
        if (StringUtils.isNull(result.getSource()))
        {
            result.setSource(0); // 0手动录入
        }
        return resultMapper.insertResult(result);
    }

    /**
     * 修改成绩
     *
     * @param result 成绩信息
     * @return 结果
     */
    @Override
    public int updateResult(Result result)
    {
        result.setUpdateTime(DateUtils.getNowDate());
        return resultMapper.updateResult(result);
    }

    /**
     * 确认成绩（0未完赛 1已完赛 2成绩无效）
     *
     * @param id 成绩ID
     * @param status 成绩状态
     * @return 结果
     */
    @Override
    public int confirmResult(Long id, Integer status)
    {
        Result result = resultMapper.selectResultById(id);
        if (StringUtils.isNull(result))
        {
            throw new ServiceException("成绩记录不存在");
        }
        result.setStatus(status);
        result.setUpdateTime(DateUtils.getNowDate());
        return resultMapper.updateResult(result);
    }

    /**
     * 批量删除成绩
     *
     * @param ids 需要删除的成绩ID
     * @return 结果
     */
    @Override
    public int deleteResultByIds(Long[] ids)
    {
        return resultMapper.deleteResultByIds(ids);
    }

    /**
     * 根据摄像头通过记录计算赛事成绩（系统计算，source=0）
     * 计算逻辑：对每个号码牌取各摄像头点位最早通过时间（min(pass_time)），
     * 最早=起点、最晚=终点；net_time=最晚-最早，gun_time=最晚-赛事开赛时间；
     * 已完赛（≥2个点位）按 net_time 升序排名。
     *
     * @param eventId 赛事ID
     * @return 计算完成的选手数量
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int calculateEventResults(Long eventId)
    {
        Event event = eventMapper.selectEventById(eventId);
        if (StringUtils.isNull(event))
        {
            throw new ServiceException("赛事不存在");
        }
        List<PassArrivalVo> allArrivals = passRecordMapper.selectAllArrivalsByEvent(eventId);
        // 按号码牌分组（SQL 已按 bib、到达时间排序）
        Map<String, List<PassArrivalVo>> grouped = new LinkedHashMap<>();
        for (PassArrivalVo arrival : allArrivals)
        {
            grouped.computeIfAbsent(arrival.getBib(), k -> new ArrayList<>()).add(arrival);
        }
        int computed = 0;
        int skipped = 0;
        for (Map.Entry<String, List<PassArrivalVo>> entry : grouped.entrySet())
        {
            String bib = entry.getKey();
            List<PassArrivalVo> arrivals = entry.getValue();
            // 通过报名记录将号码牌归属到选手
            Registration registration = registrationMapper.selectByEventAndBib(eventId, bib);
            if (StringUtils.isNull(registration) || StringUtils.isNull(registration.getPersonId()))
            {
                skipped++;
                continue;
            }
            long earliest = arrivals.get(0).getArriveTime().getTime();
            long latest = arrivals.get(arrivals.size() - 1).getArriveTime().getTime();
            // 1已完赛（同时具备起点与终点），0未完赛
            int status = arrivals.size() >= 2 ? 1 : 0;
            String netTimeText = formatDuration(latest - earliest);
            String gunTimeText = StringUtils.isNotNull(event.getStartTime())
                    ? formatDuration(latest - event.getStartTime().getTime()) : netTimeText;
            Result exist = resultMapper.selectByEventAndPerson(eventId, registration.getPersonId());
            if (StringUtils.isNull(exist))
            {
                Result result = new Result();
                result.setEventId(eventId);
                result.setPersonId(registration.getPersonId());
                result.setRegistrationId(registration.getId());
                result.setBib(bib);
                result.setGunTime(gunTimeText);
                result.setNetTime(netTimeText);
                // 摄像头点位暂无距离数据，平均配速置空
                result.setAvgPace(null);
                result.setStatus(status);
                result.setSource(0);
                result.setCreateTime(DateUtils.getNowDate());
                resultMapper.insertResult(result);
            }
            else
            {
                exist.setRegistrationId(registration.getId());
                exist.setBib(bib);
                exist.setGunTime(gunTimeText);
                exist.setNetTime(netTimeText);
                exist.setStatus(status);
                exist.setSource(0);
                exist.setUpdateTime(DateUtils.getNowDate());
                resultMapper.updateResult(exist);
            }
            computed++;
        }
        // 排名：已完赛选手按净成绩升序
        Result query = new Result();
        query.setEventId(eventId);
        List<Result> eventResults = resultMapper.selectResultList(query);
        List<Result> finished = new ArrayList<>();
        for (Result item : eventResults)
        {
            if (Integer.valueOf(1).equals(item.getStatus()) && StringUtils.isNotEmpty(item.getNetTime()))
            {
                finished.add(item);
            }
            else
            {
                item.setTotalRank(null);
            }
        }
        finished.sort(Comparator.comparing(Result::getNetTime));
        int rank = 1;
        for (Result item : finished)
        {
            item.setTotalRank(rank++);
            resultMapper.updateResult(item);
        }
        if (skipped > 0)
        {
            log.info("计算赛事[{}]成绩：完成{}条，跳过{}条无法归属选手的号码牌", eventId, computed, skipped);
        }
        return computed;
    }

    /**
     * 查询成绩详情（含分段明细）
     *
     * @param id 成绩ID
     * @return 成绩信息（含 segments 分段明细）
     */
    @Override
    public Result selectResultDetail(Long id)
    {
        Result result = resultMapper.selectResultById(id);
        if (StringUtils.isNull(result))
        {
            return null;
        }
        if (StringUtils.isNotEmpty(result.getBib()) && StringUtils.isNotNull(result.getEventId()))
        {
            List<PassArrivalVo> arrivals = passRecordMapper.selectArrivalsByEventAndBib(result.getEventId(), result.getBib());
            result.setSegments(buildSegments(arrivals));
        }
        return result;
    }

    /**
     * 根据点位到达时间构建分段明细（首段无前一节点，用时为空）
     *
     * @param arrivals 点位到达时间列表（按到达时间升序）
     * @return 分段明细
     */
    private List<SegmentVo> buildSegments(List<PassArrivalVo> arrivals)
    {
        List<SegmentVo> segments = new ArrayList<>();
        for (int i = 0; i < arrivals.size(); i++)
        {
            PassArrivalVo arrival = arrivals.get(i);
            SegmentVo segment = new SegmentVo();
            segment.setCameraId(arrival.getCameraId());
            segment.setArriveTime(arrival.getArriveTime());
            if (i > 0)
            {
                long duration = arrival.getArriveTime().getTime() - arrivals.get(i - 1).getArriveTime().getTime();
                segment.setSegmentDuration(duration);
                segment.setSegmentDurationText(formatDuration(duration));
            }
            segments.add(segment);
        }
        return segments;
    }

    /**
     * 将毫秒时长格式化为 HH:mm:ss.SS
     *
     * @param millis 毫秒数
     * @return 格式化时长
     */
    private String formatDuration(long millis)
    {
        if (millis < 0)
        {
            millis = 0;
        }
        long totalSeconds = millis / 1000;
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        long hundredths = (millis % 1000) / 10;
        return String.format("%02d:%02d:%02d.%02d", hours, minutes, seconds, hundredths);
    }
}
