package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.Event;
import com.ruoyi.system.domain.Registration;
import com.ruoyi.system.mapper.EventMapper;
import com.ruoyi.system.mapper.RegistrationMapper;
import com.ruoyi.system.service.IRegistrationService;

/**
 * 赛事报名管理 服务层实现
 *
 * @author ruoyi
 */
@Service("registrationService")
public class RegistrationServiceImpl implements IRegistrationService
{
    @Autowired
    private RegistrationMapper registrationMapper;

    @Autowired
    private EventMapper eventMapper;

    /**
     * 查询报名记录列表
     *
     * @param registration 报名信息
     * @return 报名集合
     */
    @Override
    public List<Registration> selectRegistrationList(Registration registration)
    {
        return registrationMapper.selectRegistrationList(registration);
    }

    /**
     * 查询报名记录信息
     *
     * @param id 报名ID
     * @return 报名信息
     */
    @Override
    public Registration selectRegistrationById(Long id)
    {
        return registrationMapper.selectRegistrationById(id);
    }

    /**
     * 创建报名（报名并发控制）
     * 并发说明：当前 selectEventById 无 SELECT ... FOR UPDATE 行锁，
     * 极端并发情况下依赖 uk_person_event 唯一索引兜底防重复报名，
     * registered 计数通过数据库原子自增保持最终一致；行锁优化为后续迭代项。
     *
     * @param personId 参赛人员ID
     * @param eventId 赛事ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createRegistration(Long personId, Long eventId)
    {
        // 1. 校验赛事存在
        Event event = eventMapper.selectEventById(eventId);
        if (StringUtils.isNull(event))
        {
            throw new ServiceException("赛事不存在");
        }
        // 2. 校验报名是否开放
        if (!Integer.valueOf(1).equals(event.getSignupOpen()))
        {
            throw new ServiceException("报名未开放");
        }
        // 3. 校验赛事处于报名阶段（1报名中）
        if (!Integer.valueOf(1).equals(event.getStatus()))
        {
            throw new ServiceException("赛事不在报名阶段");
        }
        // 4. 校验当前时间在报名时间范围内
        Date now = DateUtils.getNowDate();
        if (StringUtils.isNotNull(event.getSignupStart()) && now.before(event.getSignupStart()))
        {
            throw new ServiceException("不在报名时间范围内");
        }
        if (StringUtils.isNotNull(event.getSignupEnd()) && now.after(event.getSignupEnd()))
        {
            throw new ServiceException("不在报名时间范围内");
        }
        // 5. 校验名额是否已满
        int registered = StringUtils.isNull(event.getRegistered()) ? 0 : event.getRegistered();
        if (StringUtils.isNotNull(event.getTotalQuota()) && registered >= event.getTotalQuota())
        {
            throw new ServiceException("名额已满");
        }
        // 6. 重复报名校验：已退赛（2）允许重新报名
        Registration exist = registrationMapper.selectByPersonAndEvent(personId, eventId);
        if (StringUtils.isNotNull(exist) && !Integer.valueOf(2).equals(exist.getStatus()))
        {
            throw new ServiceException("请勿重复报名");
        }
        // 7. 报名落库：并发极端情况下依赖 uk_person_event 唯一索引兜底
        Registration registration = new Registration();
        registration.setPersonId(personId);
        registration.setEventId(eventId);
        registration.setStatus(0); // 0待审核
        registration.setCreateTime(DateUtils.getNowDate());
        try
        {
            registrationMapper.insertRegistration(registration);
        }
        catch (DuplicateKeyException e)
        {
            ServiceException exception = new ServiceException("请勿重复报名");
            exception.initCause(e);
            throw exception;
        }
        // 8. 赛事已报名人数 +1（数据库原子自增，保证计数最终一致）
        eventMapper.increaseRegistered(eventId);
        return 1;
    }

    /**
     * 审核报名
     *
     * @param id 报名ID
     * @param status 审核状态
     * @return 结果
     */
    @Override
    public int reviewRegistration(Long id, Integer status)
    {
        Registration registration = registrationMapper.selectRegistrationById(id);
        if (StringUtils.isNull(registration))
        {
            throw new ServiceException("报名记录不存在");
        }
        registration.setStatus(status);
        registration.setUpdateTime(DateUtils.getNowDate());
        return registrationMapper.updateRegistration(registration);
    }

    /**
     * 退赛处理：报名状态置为已退赛并回退赛事已报名人数
     *
     * @param id 报名ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int refundRegistration(Long id)
    {
        Registration registration = registrationMapper.selectRegistrationById(id);
        if (StringUtils.isNull(registration))
        {
            throw new ServiceException("报名记录不存在");
        }
        if (Integer.valueOf(2).equals(registration.getStatus()))
        {
            throw new ServiceException("该报名已退赛");
        }
        registration.setStatus(2); // 2已退赛
        registration.setUpdateTime(DateUtils.getNowDate());
        int rows = registrationMapper.updateRegistration(registration);
        // 赛事已报名人数 -1
        eventMapper.decreaseRegistered(registration.getEventId());
        return rows;
    }

    /**
     * 查询我的报名记录
     *
     * @param personId 参赛人员ID
     * @return 报名集合
     */
    @Override
    public List<Registration> selectMyRegistrations(Long personId)
    {
        return registrationMapper.selectMyRegistrations(personId);
    }

    /**
     * 管理员绑定赛事（报名）：管理端补录场景，跳过报名时间窗口与报名开关校验，
     * 保留名额校验与重复报名防重（已退赛允许重新绑定）
     *
     * @param personId 参赛人员ID
     * @param eventId 赛事ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int bindPersonEvent(Long personId, Long eventId)
    {
        // 1. 校验赛事存在
        Event event = eventMapper.selectEventById(eventId);
        if (StringUtils.isNull(event))
        {
            throw new ServiceException("赛事不存在");
        }
        // 2. 校验名额是否已满
        int registered = StringUtils.isNull(event.getRegistered()) ? 0 : event.getRegistered();
        if (StringUtils.isNotNull(event.getTotalQuota()) && registered >= event.getTotalQuota())
        {
            throw new ServiceException("名额已满");
        }
        // 3. 重复绑定校验：已退赛（2）允许重新绑定
        Registration exist = registrationMapper.selectByPersonAndEvent(personId, eventId);
        if (StringUtils.isNotNull(exist) && !Integer.valueOf(2).equals(exist.getStatus()))
        {
            throw new ServiceException("该参赛用户已绑定该赛事");
        }
        // 4. 报名落库：并发极端情况下依赖 uk_person_event 唯一索引兜底
        Registration registration = new Registration();
        registration.setPersonId(personId);
        registration.setEventId(eventId);
        registration.setStatus(0); // 0已报名（待审核）
        registration.setCreateTime(DateUtils.getNowDate());
        try
        {
            registrationMapper.insertRegistration(registration);
        }
        catch (DuplicateKeyException e)
        {
            ServiceException exception = new ServiceException("该参赛用户已绑定该赛事");
            exception.initCause(e);
            throw exception;
        }
        // 5. 赛事已报名人数 +1
        eventMapper.increaseRegistered(eventId);
        return 1;
    }

    /**
     * 管理员解绑赛事（退赛处理）：报名状态置为已退赛并回退赛事已报名人数
     *
     * @param personId 参赛人员ID
     * @param eventId 赛事ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int unbindPersonEvent(Long personId, Long eventId)
    {
        Registration registration = registrationMapper.selectByPersonAndEvent(personId, eventId);
        if (StringUtils.isNull(registration))
        {
            throw new ServiceException("该参赛用户未绑定该赛事");
        }
        if (Integer.valueOf(2).equals(registration.getStatus()))
        {
            throw new ServiceException("该赛事已解绑");
        }
        registration.setStatus(2); // 2已退赛
        registration.setUpdateTime(DateUtils.getNowDate());
        int rows = registrationMapper.updateRegistration(registration);
        // 赛事已报名人数 -1
        eventMapper.decreaseRegistered(registration.getEventId());
        return rows;
    }

    /**
     * 批量删除报名记录
     *
     * @param ids 需要删除的报名ID
     * @return 结果
     */
    @Override
    public int deleteRegistrationByIds(Long[] ids)
    {
        return registrationMapper.deleteRegistrationByIds(ids);
    }
}
