package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.Person;
import com.ruoyi.system.mapper.PersonMapper;
import com.ruoyi.system.service.IPersonService;

/**
 * 参赛人员管理 服务层实现
 *
 * @author ruoyi
 */
@Service("personService")
public class PersonServiceImpl implements IPersonService
{
    @Autowired
    private PersonMapper personMapper;

    /**
     * 查询参赛人员列表
     *
     * @param person 参赛人员信息
     * @return 参赛人员集合
     */
    @Override
    public List<Person> selectPersonList(Person person)
    {
        return personMapper.selectPersonList(person);
    }

    /**
     * 查询参赛人员信息
     *
     * @param id 参赛人员ID
     * @return 参赛人员信息
     */
    @Override
    public Person selectPersonById(Long id)
    {
        return personMapper.selectPersonById(id);
    }

    /**
     * 根据微信openid查询参赛人员信息
     *
     * @param openid 微信openid
     * @return 参赛人员信息
     */
    @Override
    public Person selectPersonByOpenid(String openid)
    {
        return personMapper.selectPersonByOpenid(openid);
    }

    /**
     * 新增参赛人员（openid非必填，手机号重复校验）
     *
     * @param person 参赛人员信息
     * @return 结果
     */
    @Override
    public int insertPerson(Person person)
    {
        if (StringUtils.isNull(person.getStatus()))
        {
            person.setStatus(0); // 0正常
        }
        person.setCreateTime(DateUtils.getNowDate());
        checkPhoneUnique(person);
        return personMapper.insertPerson(person);
    }

    /**
     * 修改参赛人员
     *
     * @param person 参赛人员信息
     * @return 结果
     */
    @Override
    public int updatePerson(Person person)
    {
        person.setUpdateTime(DateUtils.getNowDate());
        checkPhoneUnique(person);
        return personMapper.updatePerson(person);
    }

    /**
     * 修改参赛人员状态
     *
     * @param id 参赛人员ID
     * @param status 状态
     * @return 结果
     */
    @Override
    public int changePersonStatus(Long id, Integer status)
    {
        Person person = personMapper.selectPersonById(id);
        if (StringUtils.isNull(person))
        {
            throw new ServiceException("参赛人员不存在");
        }
        person.setStatus(status);
        person.setUpdateTime(DateUtils.getNowDate());
        return personMapper.updatePerson(person);
    }

    /**
     * 批量删除参赛人员
     *
     * @param ids 需要删除的参赛人员ID
     * @return 结果
     */
    @Override
    public int deletePersonByIds(Long[] ids)
    {
        return personMapper.deletePersonByIds(ids);
    }

    /**
     * 校验手机号是否重复（排除自身）
     *
     * @param person 参赛人员信息
     */
    private void checkPhoneUnique(Person person)
    {
        if (StringUtils.isEmpty(person.getPhone()))
        {
            return;
        }
        Person query = new Person();
        query.setPhone(person.getPhone());
        List<Person> list = personMapper.selectPersonList(query);
        if (StringUtils.isNotEmpty(list))
        {
            Long personId = StringUtils.isNull(person.getId()) ? -1L : person.getId();
            for (Person item : list)
            {
                if (item.getId().longValue() != personId.longValue())
                {
                    throw new ServiceException("手机号已存在");
                }
            }
        }
    }
}
