package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Event;

/**
 * 赛事管理 服务层
 *
 * @author ruoyi
 */
public interface IEventService
{
    /**
     * 查询赛事列表
     *
     * @param event 赛事信息
     * @return 赛事集合
     */
    public List<Event> selectEventList(Event event);

    /**
     * 查询赛事信息
     *
     * @param id 赛事ID
     * @return 赛事信息
     */
    public Event selectEventById(Long id);

    /**
     * 新增赛事
     *
     * @param event 赛事信息
     * @return 结果
     */
    public int insertEvent(Event event);

    /**
     * 修改赛事
     *
     * @param event 赛事信息
     * @return 结果
     */
    public int updateEvent(Event event);

    /**
     * 修改赛事状态（0未发布 1报名中 2进行中 3已结束）
     *
     * @param id 赛事ID
     * @param status 赛事状态
     * @return 结果
     */
    public int changeEventStatus(Long id, Integer status);

    /**
     * 批量删除赛事
     *
     * @param ids 需要删除的赛事ID
     * @return 结果
     */
    public int deleteEventByIds(Long[] ids);
}
