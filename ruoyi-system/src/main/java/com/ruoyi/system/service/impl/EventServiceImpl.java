package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.Event;
import com.ruoyi.system.mapper.EventMapper;
import com.ruoyi.system.service.IEventService;

/**
 * 赛事管理 服务层实现
 *
 * @author ruoyi
 */
@Service("eventService")
public class EventServiceImpl implements IEventService
{
    @Autowired
    private EventMapper eventMapper;

    /**
     * 查询赛事列表
     *
     * @param event 赛事信息
     * @return 赛事集合
     */
    @Override
    public List<Event> selectEventList(Event event)
    {
        return eventMapper.selectEventList(event);
    }

    /**
     * 查询赛事信息
     *
     * @param id 赛事ID
     * @return 赛事信息
     */
    @Override
    public Event selectEventById(Long id)
    {
        return eventMapper.selectEventById(id);
    }

    /**
     * 新增赛事
     *
     * @param event 赛事信息
     * @return 结果
     */
    @Override
    public int insertEvent(Event event)
    {
        event.setCreateTime(DateUtils.getNowDate());
        if (StringUtils.isNull(event.getStatus()))
        {
            event.setStatus(0); // 0未发布
        }
        if (StringUtils.isNull(event.getRegistered()))
        {
            event.setRegistered(0);
        }
        if (StringUtils.isNull(event.getSignupOpen()))
        {
            event.setSignupOpen(1); // 默认开放报名
        }
        return eventMapper.insertEvent(event);
    }

    /**
     * 修改赛事
     *
     * @param event 赛事信息
     * @return 结果
     */
    @Override
    public int updateEvent(Event event)
    {
        event.setUpdateTime(DateUtils.getNowDate());
        return eventMapper.updateEvent(event);
    }

    /**
     * 修改赛事状态（0未发布 1报名中 2进行中 3已结束）
     * 报名中自动开放报名，已结束自动关闭报名
     *
     * @param id 赛事ID
     * @param status 赛事状态
     * @return 结果
     */
    @Override
    public int changeEventStatus(Long id, Integer status)
    {
        Event event = eventMapper.selectEventById(id);
        if (StringUtils.isNull(event))
        {
            throw new ServiceException("赛事不存在");
        }
        event.setStatus(status);
        event.setUpdateTime(DateUtils.getNowDate());
        if (Integer.valueOf(1).equals(status))
        {
            // 报名中：开放报名
            event.setSignupOpen(1);
        }
        else if (Integer.valueOf(3).equals(status))
        {
            // 已结束：关闭报名
            event.setSignupOpen(0);
        }
        return eventMapper.updateEvent(event);
    }

    /**
     * 批量删除赛事
     *
     * @param ids 需要删除的赛事ID
     * @return 结果
     */
    @Override
    public int deleteEventByIds(Long[] ids)
    {
        return eventMapper.deleteEventByIds(ids);
    }
}
