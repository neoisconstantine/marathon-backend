package com.ruoyi.framework.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.Person;
import com.ruoyi.system.mapper.PersonMapper;

/**
 * 微信小程序登录服务（code换取openid，人员不存在时自动注册，签发独立小程序JWT）
 *
 * @author ruoyi
 */
@Component
public class WxLoginService
{
    private static final Logger log = LoggerFactory.getLogger(WxLoginService.class);

    /** 微信jscode2session接口地址 */
    private static final String JSCODE2SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session";

    /** 小程序appid（留空时使用开发模式） */
    @Value("${wx.appid:}")
    private String appid;

    /** 小程序密钥 */
    @Value("${wx.secret:}")
    private String secret;

    @Autowired
    private WxTokenService wxTokenService;

    @Autowired
    private PersonMapper personMapper;

    /**
     * 微信小程序登录
     *
     * @param code 微信登录code
     * @return 小程序JWT令牌
     */
    public String wxLogin(String code)
    {
        String openid = getOpenid(code);
        Person person = personMapper.selectPersonByOpenid(openid);
        if (person == null)
        {
            // 首次登录自动注册人员
            person = new Person();
            person.setOpenid(openid);
            person.setName("");
            person.setStatus(0);
            personMapper.insertPerson(person);
            person = personMapper.selectPersonByOpenid(openid);
        }
        return wxTokenService.createToken(person.getId(), openid);
    }

    /**
     * 通过code换取openid（未配置appid时使用开发模式，便于本地联调）
     *
     * @param code 微信登录code
     * @return openid
     */
    private String getOpenid(String code)
    {
        if (StringUtils.isBlank(appid))
        {
            log.warn("微信未配置appid，使用开发模式openid");
            return "mock_" + code;
        }
        String param = "appid=" + appid + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
        String result = HttpUtils.sendGet(JSCODE2SESSION_URL, param);
        JSONObject json = JSON.parseObject(result);
        if (json != null && json.containsKey("errcode") && json.getIntValue("errcode") != 0)
        {
            throw new ServiceException("微信登录失败: " + json.getString("errmsg"));
        }
        String openid = json == null ? null : json.getString("openid");
        if (StringUtils.isBlank(openid))
        {
            throw new ServiceException("微信登录失败: 未获取到openid");
        }
        return openid;
    }
}
