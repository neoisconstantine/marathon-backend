package com.ruoyi.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.system.domain.Registration;

/**
 * 报名 数据层
 * 
 * @author ruoyi
 */
public interface RegistrationMapper
{
    /**
     * 通过ID查询报名
     * 
     * @param id 报名ID
     * @return 报名信息
     */
    public Registration selectRegistrationById(Long id);

    /**
     * 查询报名列表
     * 
     * @param registration 报名信息
     * @return 报名集合
     */
    public List<Registration> selectRegistrationList(Registration registration);

    /**
     * 查询我的报名列表
     * 
     * @param personId 参赛用户ID
     * @return 报名集合
     */
    public List<Registration> selectMyRegistrations(Long personId);

    /**
     * 通过用户ID和赛事ID查询报名
     * 
     * @param personId 参赛用户ID
     * @param eventId 赛事ID
     * @return 报名信息
     */
    public Registration selectByPersonAndEvent(@Param("personId") Long personId, @Param("eventId") Long eventId);

    /**
     * 通过赛事ID和号码牌查询报名
     *
     * @param eventId 赛事ID
     * @param bib 号码牌
     * @return 报名信息
     */
    public Registration selectByEventAndBib(@Param("eventId") Long eventId, @Param("bib") String bib);

    /**
     * 通过赛事ID和手机号查询报名（join person 取手机号）
     *
     * @param eventId 赛事ID
     * @param phone 手机号
     * @return 报名信息
     */
    public Registration selectByEventAndPhone(@Param("eventId") Long eventId, @Param("phone") String phone);

    /**
     * 通过赛事ID和身份证号查询报名（join person 取身份证号）
     *
     * @param eventId 赛事ID
     * @param idCard 身份证号
     * @return 报名信息
     */
    public Registration selectByEventAndIdCard(@Param("eventId") Long eventId, @Param("idCard") String idCard);

    /**
     * 新增报名
     * 
     * @param registration 报名信息
     * @return 结果
     */
    public int insertRegistration(Registration registration);

    /**
     * 修改报名
     * 
     * @param registration 报名信息
     * @return 结果
     */
    public int updateRegistration(Registration registration);

    /**
     * 批量删除报名
     * 
     * @param ids 需要删除的报名ID
     * @return 结果
     */
    public int deleteRegistrationByIds(Long[] ids);
}
