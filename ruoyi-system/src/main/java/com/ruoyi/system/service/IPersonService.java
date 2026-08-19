package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Person;

/**
 * 参赛人员管理 服务层
 *
 * @author ruoyi
 */
public interface IPersonService
{
    /**
     * 查询参赛人员列表
     *
     * @param person 参赛人员信息
     * @return 参赛人员集合
     */
    public List<Person> selectPersonList(Person person);

    /**
     * 查询参赛人员信息
     *
     * @param id 参赛人员ID
     * @return 参赛人员信息
     */
    public Person selectPersonById(Long id);

    /**
     * 根据微信openid查询参赛人员信息
     *
     * @param openid 微信openid
     * @return 参赛人员信息
     */
    public Person selectPersonByOpenid(String openid);

    /**
     * 新增参赛人员
     *
     * @param person 参赛人员信息
     * @return 结果
     */
    public int insertPerson(Person person);

    /**
     * 修改参赛人员
     *
     * @param person 参赛人员信息
     * @return 结果
     */
    public int updatePerson(Person person);

    /**
     * 修改参赛人员状态
     *
     * @param id 参赛人员ID
     * @param status 状态
     * @return 结果
     */
    public int changePersonStatus(Long id, Integer status);

    /**
     * 批量删除参赛人员
     *
     * @param ids 需要删除的参赛人员ID
     * @return 结果
     */
    public int deletePersonByIds(Long[] ids);
}
