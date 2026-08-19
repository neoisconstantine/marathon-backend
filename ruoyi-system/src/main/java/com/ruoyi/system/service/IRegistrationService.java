package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Registration;

/**
 * 赛事报名管理 服务层
 *
 * @author ruoyi
 */
public interface IRegistrationService
{
    /**
     * 查询报名记录列表
     *
     * @param registration 报名信息
     * @return 报名集合
     */
    public List<Registration> selectRegistrationList(Registration registration);

    /**
     * 查询报名记录信息
     *
     * @param id 报名ID
     * @return 报名信息
     */
    public Registration selectRegistrationById(Long id);

    /**
     * 创建报名（含名额与重复报名校验）
     *
     * @param personId 参赛人员ID
     * @param eventId 赛事ID
     * @return 结果
     */
    public int createRegistration(Long personId, Long eventId);

    /**
     * 审核报名
     *
     * @param id 报名ID
     * @param status 审核状态
     * @return 结果
     */
    public int reviewRegistration(Long id, Integer status);

    /**
     * 退赛处理
     *
     * @param id 报名ID
     * @return 结果
     */
    public int refundRegistration(Long id);

    /**
     * 查询我的报名记录
     *
     * @param personId 参赛人员ID
     * @return 报名集合
     */
    public List<Registration> selectMyRegistrations(Long personId);

    /**
     * 管理员绑定赛事（报名）：不校验报名时间窗口与报名开关，
     * 仅校验名额与重复报名（用于管理端为参赛用户补录绑定赛事）
     *
     * @param personId 参赛人员ID
     * @param eventId 赛事ID
     * @return 结果
     */
    public int bindPersonEvent(Long personId, Long eventId);

    /**
     * 管理员解绑赛事（退赛处理）
     *
     * @param personId 参赛人员ID
     * @param eventId 赛事ID
     * @return 结果
     */
    public int unbindPersonEvent(Long personId, Long eventId);

    /**
     * 新增报名（管理端直接录入）
     *
     * @param registration 报名信息
     * @return 结果
     */
    public int insertRegistration(Registration registration);

    /**
     * 修改报名（管理端编辑：号码布/状态等）
     *
     * @param registration 报名信息
     * @return 结果
     */
    public int updateRegistration(Registration registration);

    /**
     * 批量删除报名记录
     *
     * @param ids 需要删除的报名ID
     * @return 结果
     */
    public int deleteRegistrationByIds(Long[] ids);
}
