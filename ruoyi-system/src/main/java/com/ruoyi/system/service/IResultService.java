package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Result;

/**
 * 成绩管理 服务层
 *
 * @author ruoyi
 */
public interface IResultService
{
    /**
     * 查询成绩列表
     *
     * @param result 成绩信息
     * @return 成绩集合
     */
    public List<Result> selectResultList(Result result);

    /**
     * 查询成绩信息
     *
     * @param id 成绩ID
     * @return 成绩信息
     */
    public Result selectResultById(Long id);

    /**
     * 查询我的成绩列表
     *
     * @param personId 参赛人员ID
     * @return 成绩集合
     */
    public List<Result> selectMyResults(Long personId);

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
     * 确认成绩（0未完赛 1已完赛 2成绩无效）
     *
     * @param id 成绩ID
     * @param status 成绩状态
     * @return 结果
     */
    public int confirmResult(Long id, Integer status);

    /**
     * 批量删除成绩
     *
     * @param ids 需要删除的成绩ID
     * @return 结果
     */
    public int deleteResultByIds(Long[] ids);
}
