package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.Alarm;
import com.ruoyi.system.mapper.AlarmMapper;
import com.ruoyi.system.service.IAlarmService;

/**
 * 报警 服务层实现
 *
 * @author ruoyi
 */
@Service("alarmService")
public class AlarmServiceImpl implements IAlarmService
{
    @Autowired
    private AlarmMapper alarmMapper;

    /**
     * 查询报警列表
     *
     * @param alarm 报警信息
     * @return 报警集合
     */
    @Override
    public List<Alarm> selectAlarmList(Alarm alarm)
    {
        return alarmMapper.selectAlarmList(alarm);
    }

    /**
     * 查询报警信息
     *
     * @param id 报警ID
     * @return 报警信息
     */
    @Override
    public Alarm selectAlarmById(Long id)
    {
        return alarmMapper.selectAlarmById(id);
    }

    /**
     * 新增报警
     *
     * @param alarm 报警信息
     * @return 结果
     */
    @Override
    public int insertAlarm(Alarm alarm)
    {
        alarm.setCreateTime(DateUtils.getNowDate());
        if (StringUtils.isNull(alarm.getStatus()))
        {
            alarm.setStatus(0); // 0未处理
        }
        if (StringUtils.isNull(alarm.getLevel()))
        {
            alarm.setLevel(1); // 默认提示
        }
        return alarmMapper.insertAlarm(alarm);
    }

    /**
     * 修改报警
     *
     * @param alarm 报警信息
     * @return 结果
     */
    @Override
    public int updateAlarm(Alarm alarm)
    {
        alarm.setUpdateTime(DateUtils.getNowDate());
        return alarmMapper.updateAlarm(alarm);
    }

    /**
     * 批量删除报警
     *
     * @param ids 需要删除的报警ID
     * @return 结果
     */
    @Override
    public int deleteAlarmByIds(Long[] ids)
    {
        return alarmMapper.deleteAlarmByIds(ids);
    }
}