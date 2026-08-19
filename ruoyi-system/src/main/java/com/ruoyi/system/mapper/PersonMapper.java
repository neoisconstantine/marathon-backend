package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.Person;

/**
 * 参赛用户 数据层
 * 
 * @author ruoyi
 */
public interface PersonMapper
{
    /**
     * 通过ID查询参赛用户
     * 
     * @param id 用户ID
     * @return 参赛用户信息
     */
    public Person selectPersonById(Long id);

    /**
     * 通过openid查询参赛用户
     * 
     * @param openid 微信openid
     * @return 参赛用户信息
     */
    public Person selectPersonByOpenid(String openid);

    /**
     * 查询参赛用户列表
     * 
     * @param person 参赛用户信息
     * @return 参赛用户集合
     */
    public List<Person> selectPersonList(Person person);

    /**
     * 新增参赛用户
     * 
     * @param person 参赛用户信息
     * @return 结果
     */
    public int insertPerson(Person person);

    /**
     * 修改参赛用户
     * 
     * @param person 参赛用户信息
     * @return 结果
     */
    public int updatePerson(Person person);

    /**
     * 删除参赛用户
     * 
     * @param id 用户ID
     * @return 结果
     */
    public int deletePersonById(Long id);

    /**
     * 批量删除参赛用户
     * 
     * @param ids 需要删除的用户ID
     * @return 结果
     */
    public int deletePersonByIds(Long[] ids);
}
