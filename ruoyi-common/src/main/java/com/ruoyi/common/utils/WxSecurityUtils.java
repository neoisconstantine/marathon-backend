package com.ruoyi.common.utils;

import com.ruoyi.common.exception.ServiceException;

/**
 * 小程序用户安全工具类（当前登录人员ID存于ThreadLocal，由WxAuthInterceptor负责设置与清理）
 *
 * @author ruoyi
 */
public class WxSecurityUtils
{
    /** 当前登录人员ID */
    private static final ThreadLocal<Long> PERSON_ID = new ThreadLocal<>();

    /**
     * 设置当前登录人员ID
     *
     * @param personId 人员ID
     */
    public static void setPersonId(Long personId)
    {
        PERSON_ID.set(personId);
    }

    /**
     * 获取当前登录人员ID
     *
     * @return 人员ID
     */
    public static Long getPersonId()
    {
        Long personId = PERSON_ID.get();
        if (personId == null)
        {
            throw new ServiceException("未登录或登录已过期");
        }
        return personId;
    }

    /**
     * 清理ThreadLocal（请求结束时调用，防止线程复用导致的数据串用）
     */
    public static void clear()
    {
        PERSON_ID.remove();
    }
}
