package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.Result;
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
    @Autowired
    private ResultMapper resultMapper;

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
}
