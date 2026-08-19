package com.ruoyi.framework.web.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 小程序token验证处理（独立于管理端的无状态JWT，通过wx-token请求头传递）
 *
 * @author ruoyi
 */
@Component
public class WxTokenService
{
    /** 小程序令牌请求头名称 */
    public static final String WX_TOKEN_HEADER = "wx-token";

    /** 人员ID声明名称 */
    public static final String CLAIM_PERSON_ID = "wx_person_id";

    /** openid声明名称 */
    public static final String CLAIM_OPENID = "wx_openid";

    /** 令牌秘钥（与管理端共用token.secret配置） */
    @Value("${token.secret}")
    private String secret;

    /** 小程序令牌有效期（分钟，默认10080即7天） */
    @Value("${wx.expireTime:10080}")
    private int expireTime;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    /**
     * 创建小程序令牌
     *
     * @param personId 人员ID
     * @param openid 微信openid
     * @return 令牌
     */
    public String createToken(Long personId, String openid)
    {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_PERSON_ID, personId);
        claims.put(CLAIM_OPENID, openid);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(openid)
                .setExpiration(new Date(System.currentTimeMillis() + expireTime * MILLIS_MINUTE))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    public Claims parseToken(String token)
    {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从令牌中获取人员ID
     *
     * @param token 令牌
     * @return 人员ID（令牌无效时抛出异常）
     */
    public Long getPersonId(String token)
    {
        Claims claims = parseToken(token);
        Object value = claims.get(CLAIM_PERSON_ID);
        if (value instanceof Number)
        {
            return ((Number) value).longValue();
        }
        return claims.get(CLAIM_PERSON_ID, Long.class);
    }

    /**
     * 从令牌中获取openid
     *
     * @param token 令牌
     * @return openid
     */
    public String getOpenid(String token)
    {
        Claims claims = parseToken(token);
        return claims.get(CLAIM_OPENID, String.class);
    }

    /**
     * 获取请求token（从wx-token请求头读取，兼容携带Bearer前缀）
     *
     * @param request 请求对象
     * @return token
     */
    public String getToken(HttpServletRequest request)
    {
        String token = request.getHeader(WX_TOKEN_HEADER);
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX))
        {
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }
        return token;
    }
}
