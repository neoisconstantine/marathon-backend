package com.ruoyi.framework.web.service;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.UserAgentUtils;
import com.ruoyi.common.utils.ip.AddressUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
// import com.ruoyi.common.constant.CacheConstants;        // 去Redis改造：原Redis缓存key常量，暂时停用（保留便于恢复）
// import com.ruoyi.common.core.redis.RedisCache;          // 去Redis改造：原Redis缓存工具类，暂时停用（保留便于恢复）

/**
 * token验证处理
 * 
 * @author ruoyi
 */
@Component
public class TokenService
{
    private static final Logger log = LoggerFactory.getLogger(TokenService.class);

    // 令牌自定义标识
    @Value("${token.header}")
    private String header;

    // 令牌秘钥
    @Value("${token.secret}")
    private String secret;

    // 令牌有效期（默认30分钟）
    @Value("${token.expireTime}")
    private int expireTime;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Long MILLIS_MINUTE_TWENTY = 20 * 60 * 1000L;

    // 去Redis改造：登录用户信息直接放入JWT（无状态），不再使用Redis缓存
    // @Autowired
    // private RedisCache redisCache;

    /**
     * 获取用户身份信息
     * 
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request)
    {
        // 获取请求携带的令牌
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token))
        {
            try
            {
                Claims claims = parseToken(token);
                // 去Redis改造：登录用户信息直接从JWT中解析（不再从Redis读取）
                // 原逻辑：String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
                //         String userKey = getTokenKey(uuid);
                //         LoginUser user = redisCache.getCacheObject(userKey);
                //         return user;
                String loginUserJson = (String) claims.get(Constants.LOGIN_USER_KEY);
                return JSON.parseObject(loginUserJson, LoginUser.class);
            }
            catch (Exception e)
            {
                log.error("获取用户信息异常'{}'", e.getMessage());
            }
        }
        return null;
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUser loginUser)
    {
        // 去Redis改造：无状态JWT，无需设置/刷新Redis缓存
        // if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken()))
        // {
        //     refreshToken(loginUser);
        // }
    }

    /**
     * 删除用户身份信息
     */
    public void delLoginUser(String token)
    {
        // 去Redis改造：无状态JWT，无需删除Redis缓存（token到期自动失效）
        // if (StringUtils.isNotEmpty(token))
        // {
        //     String userKey = getTokenKey(token);
        //     redisCache.deleteObject(userKey);
        // }
    }

    /**
     * 创建令牌
     * 
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUser loginUser)
    {
        String token = IdUtils.fastUUID();
        loginUser.setToken(token);
        setUserAgent(loginUser);
        refreshToken(loginUser);

        Map<String, Object> claims = new HashMap<>();
        // 去Redis改造：登录用户信息（JSON）直接写入JWT，实现无状态认证
        // 原逻辑：claims.put(Constants.LOGIN_USER_KEY, token);（仅存uuid，用户信息在Redis中）
        if (loginUser.getUser() != null)
        {
            // 密码哈希不放入JWT
            loginUser.getUser().setPassword(null);
        }
        claims.put(Constants.LOGIN_USER_KEY, JSON.toJSONString(loginUser));
        claims.put(Constants.JWT_USERNAME, loginUser.getUsername());
        return createToken(claims);
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     * 
     * @param loginUser 登录信息
     * @return 令牌
     */
    public void verifyToken(LoginUser loginUser)
    {
        // 去Redis改造：无状态JWT由token自身过期时间控制，无需滑动续期
        // long expireTime = loginUser.getExpireTime();
        // long currentTime = System.currentTimeMillis();
        // if (expireTime - currentTime <= MILLIS_MINUTE_TWENTY)
        // {
        //     refreshToken(loginUser);
        // }
    }

    /**
     * 刷新令牌有效期
     * 
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser)
    {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 去Redis改造：原逻辑将loginUser缓存到Redis并设置过期时间，现无状态JWT无需缓存
        // 根据uuid将loginUser缓存
        // String userKey = getTokenKey(loginUser.getToken());
        // redisCache.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

    /**
     * 设置用户代理信息
     * 
     * @param loginUser 登录信息
     */
    public void setUserAgent(LoginUser loginUser)
    {
        String userAgent = ServletUtils.getRequest().getHeader("User-Agent");
        String ip = IpUtils.getIpAddr();
        loginUser.setIpaddr(ip);
        loginUser.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        loginUser.setBrowser(UserAgentUtils.getBrowser(userAgent));
        loginUser.setOs(UserAgentUtils.getOperatingSystem(userAgent));
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String createToken(Map<String, Object> claims)
    {
        // 去Redis改造：JWT直接设置过期时间（原来靠Redis TTL控制token过期）
        String token = Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expireTime * MILLIS_MINUTE))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
        return token;
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims parseToken(String token)
    {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token)
    {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    /**
     * 获取请求token
     *
     * @param request
     * @return token
     */
    private String getToken(HttpServletRequest request)
    {
        String token = request.getHeader(header);
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX))
        {
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }
        return token;
    }

    // 去Redis改造：无状态JWT不再需要拼接Redis缓存key，原方法暂时停用（保留便于恢复）
    // private String getTokenKey(String uuid)
    // {
    //     return CacheConstants.LOGIN_TOKEN_KEY + uuid;
    // }

    /**
     * 角色权限变更后，刷新所有持有该角色的在线用户权限
     *
     * @param roleId            变更的角色ID
     * @param permissionService 权限服务
     */
    public void refreshPermissionByRoleId(Long roleId, SysPermissionService permissionService)
    {
        // 去Redis改造：原逻辑扫描Redis中所有在线token并刷新权限，无Redis后暂时停用（登录用户权限以token生成时为准）
        // // 扫描所有在线 token
        // String pattern = CacheConstants.LOGIN_TOKEN_KEY + "*";
        // Collection<String> keys = redisCache.keys(pattern);
        // if (keys == null || keys.isEmpty())
        // {
        //     return;
        // }
        // for (String key : keys)
        // {
        //     LoginUser loginUser = redisCache.getCacheObject(key);
        //     if (loginUser == null || loginUser.getUser() == null || loginUser.getUser().isAdmin())
        //     {
        //         // 管理员拥有所有权限，跳过
        //         continue;
        //     }
        //     // 判断该用户是否拥有此角色
        //     boolean hasRole = loginUser.getUser().getRoles() != null
        //             && loginUser.getUser().getRoles().stream().anyMatch(r -> roleId.equals(r.getRoleId()));
        //     if (!hasRole)
        //     {
        //         continue;
        //     }
        //     // 刷新权限缓存
        //     loginUser.setPermissions(permissionService.getMenuPermission(loginUser.getUser()));
        //     refreshToken(loginUser);
        //     log.info("角色[{}]权限变更，已刷新在线用户[{}]的权限缓存", roleId, loginUser.getUsername());
        // }
    }
}
