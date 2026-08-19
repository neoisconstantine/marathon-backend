package com.ruoyi.framework.web.service;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import com.ruoyi.common.core.domain.entity.SysUser;
// import com.ruoyi.common.constant.CacheConstants;        // 去Redis改造：原Redis缓存key常量，暂时停用（保留便于恢复）
// import com.ruoyi.common.core.redis.RedisCache;          // 去Redis改造：原Redis缓存工具类，暂时停用（保留便于恢复）
import com.ruoyi.common.exception.user.UserPasswordNotMatchException;
import com.ruoyi.common.exception.user.UserPasswordRetryLimitExceedException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.security.context.AuthenticationContextHolder;

/**
 * 登录密码方法
 * 
 * @author ruoyi
 */
@Component
public class SysPasswordService
{
    // 去Redis改造：原Redis缓存工具类，暂时停用（保留便于恢复）
    // @Autowired
    // private RedisCache redisCache;

    @Value(value = "${user.password.maxRetryCount}")
    private int maxRetryCount;

    @Value(value = "${user.password.lockTime}")
    private int lockTime;

    /**
     * 登录账户密码错误次数缓存键名
     * 
     * @param username 用户名
     * @return 缓存键key
     */
    // 去Redis改造：无Redis后不再需要密码错误计数缓存key，原方法暂时停用（保留便于恢复）
    // private String getCacheKey(String username)
    // {
    //     return CacheConstants.PWD_ERR_CNT_KEY + username;
    // }

    public void validate(SysUser user)
    {
        Authentication usernamePasswordAuthenticationToken = AuthenticationContextHolder.getContext();
        String username = usernamePasswordAuthenticationToken.getName();
        String password = usernamePasswordAuthenticationToken.getCredentials().toString();

        // 去Redis改造：密码错误次数限制依赖Redis计数，暂时停用（保留便于恢复）
        // Integer retryCount = redisCache.getCacheObject(getCacheKey(username));
        //
        // if (retryCount == null)
        // {
        //     retryCount = 0;
        // }
        //
        // if (retryCount >= Integer.valueOf(maxRetryCount).intValue())
        // {
        //     throw new UserPasswordRetryLimitExceedException(maxRetryCount, lockTime);
        // }

        if (!matches(user, password))
        {
            // 去Redis改造：原逻辑记录密码错误次数到Redis，暂时停用
            // retryCount = retryCount + 1;
            // redisCache.setCacheObject(getCacheKey(username), retryCount, lockTime, TimeUnit.MINUTES);
            throw new UserPasswordNotMatchException();
        }
        else
        {
            clearLoginRecordCache(username);
        }
    }

    public boolean matches(SysUser user, String rawPassword)
    {
        return SecurityUtils.matchesPassword(rawPassword, user.getPassword());
    }

    public void clearLoginRecordCache(String loginName)
    {
        // 去Redis改造：原逻辑删除Redis中的密码错误记录，暂时停用（保留便于恢复）
        // if (redisCache.hasKey(getCacheKey(loginName)))
        // {
        //     redisCache.deleteObject(getCacheKey(loginName));
        // }
    }
}
