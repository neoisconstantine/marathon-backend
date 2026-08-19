package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.Alarm;

/**
 * 报警 数据层
 * 
 * @author ruoyi
 */
public interface AlarmMapper
{
    /**
     * 通过ID查询报警
     * 
     * @param id 报警ID
     * @return 报警信息
     */
    public Alarm selectAlarmById(Long id);

    /**
     * 查询报警列表
     * 
     * @param alarm 报警信息
     * @return 报警集合
     */
    public List<Alarm> selectAlarmList(Alarm alarm);

    /**
     * 新增报警
     * 
     * @param alarm 报警信息
     * @return 结果
     */
    public int insertAlarm(Alarm alarm);

    /**
     * 修改报警
     * 
     * @param alarm 报警信息
     * @return 结果
     */
    public int updateAlarm(Alarm alarm);

    /**
     * 删除报警
     * 
     * @param id 报警ID
     * @return 结果
     */
    public int deleteAlarmById(Long id);

    /**
     * 批量删除报警
     * 
     * @param ids 需要删除的报警ID
     * @return 结果
     */
    public int deleteAlarmByIds(Long[] ids);
}
