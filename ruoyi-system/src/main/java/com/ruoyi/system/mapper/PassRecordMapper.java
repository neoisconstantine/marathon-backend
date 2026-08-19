package com.ruoyi.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.system.domain.PassRecord;
import com.ruoyi.system.domain.vo.PassArrivalVo;

/**
 * 计时打卡记录 数据层
 * 
 * @author ruoyi
 */
public interface PassRecordMapper
{
    /**
     * 通过ID查询计时打卡记录
     * 
     * @param id 打卡记录ID
     * @return 计时打卡记录信息
     */
    public PassRecord selectPassRecordById(Long id);

    /**
     * 查询计时打卡记录列表
     * 
     * @param passRecord 计时打卡记录信息
     * @return 计时打卡记录集合
     */
    public List<PassRecord> selectPassRecordList(PassRecord passRecord);

    /**
     * 新增计时打卡记录
     * 
     * @param passRecord 计时打卡记录信息
     * @return 结果
     */
    public int insertPassRecord(PassRecord passRecord);

    /**
     * 批量新增计时打卡记录
     * 
     * @param list 计时打卡记录集合
     * @return 结果
     */
    public int batchInsertPassRecord(List<PassRecord> list);

    /**
     * 批量删除计时打卡记录
     *
     * @param ids 需要删除的打卡记录ID
     * @return 结果
     */
    public int deletePassRecordByIds(Long[] ids);

    /**
     * 查询某赛事某号码牌在各摄像头的最早到达时间（用于成绩计算）
     *
     * @param eventId 赛事ID
     * @param bib 号码牌
     * @return 点位到达时间集合（按到达时间升序）
     */
    public List<PassArrivalVo> selectArrivalsByEventAndBib(@Param("eventId") Long eventId, @Param("bib") String bib);

    /**
     * 查询某赛事所有号码牌在各摄像头的最早到达时间（用于批量成绩计算）
     *
     * @param eventId 赛事ID
     * @return 点位到达时间集合（按号码牌、到达时间升序）
     */
    public List<PassArrivalVo> selectAllArrivalsByEvent(@Param("eventId") Long eventId);
}
