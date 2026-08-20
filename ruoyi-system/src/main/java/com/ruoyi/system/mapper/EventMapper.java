package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.Event;

/**
 * 赛事 数据层
 * 
 * @author ruoyi
 */
public interface EventMapper
{
    /**
     * 通过ID查询赛事
     * 
     * @param id 赛事ID
     * @return 赛事信息
     */
    public Event selectEventById(Long id);

    /**
     * 通过赛事名称查询赛事（摄像头导入时按名称解析 eventId）
     * 
     * @param name 赛事名称
     * @return 赛事信息
     */
    public Event selectEventByName(String name);

    /**
     * 查询赛事列表
     * 
     * @param event 赛事信息
     * @return 赛事集合
     */
    public List<Event> selectEventList(Event event);

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
     * 赛事已报名人数加一
     * 
     * @param id 赛事ID
     * @return 结果
     */
    public int increaseRegistered(Long id);

    /**
     * 赛事已报名人数减一
     * 
     * @param id 赛事ID
     * @return 结果
     */
    public int decreaseRegistered(Long id);

    /**
     * 删除赛事
     * 
     * @param id 赛事ID
     * @return 结果
     */
    public int deleteEventById(Long id);

    /**
     * 批量删除赛事
     * 
     * @param ids 需要删除的赛事ID
     * @return 结果
     */
    public int deleteEventByIds(Long[] ids);
}
