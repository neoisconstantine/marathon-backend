package com.ruoyi.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.system.domain.Result;

/**
 * 成绩 数据层
 * 
 * @author ruoyi
 */
public interface ResultMapper
{
    /**
     * 通过ID查询成绩
     * 
     * @param id 成绩ID
     * @return 成绩信息
     */
    public Result selectResultById(Long id);

    /**
     * 查询成绩列表
     * 
     * @param result 成绩信息
     * @return 成绩集合
     */
    public List<Result> selectResultList(Result result);

    /**
     * 查询我的成绩列表
     * 
     * @param personId 参赛用户ID
     * @return 成绩集合
     */
    public List<Result> selectMyResults(Long personId);

    /**
     * 通过赛事ID和用户ID查询成绩
     * 
     * @param eventId 赛事ID
     * @param personId 参赛用户ID
     * @return 成绩信息
     */
    public Result selectByEventAndPerson(@Param("eventId") Long eventId, @Param("personId") Long personId);

    /**
     * 新增成绩
     * 
     * @param result 成绩信息
     * @return 结果
     */
    public int insertResult(Result result);

    /**
     * 修改成绩
     * 
     * @param result 成绩信息
     * @return 结果
     */
    public int updateResult(Result result);

    /**
     * 批量删除成绩
     * 
     * @param ids 需要删除的成绩ID
     * @return 结果
     */
    public int deleteResultByIds(Long[] ids);
}
