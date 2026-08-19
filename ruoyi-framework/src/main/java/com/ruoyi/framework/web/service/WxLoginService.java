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

    /** 微信 access_token 接口地址 */
    private static final String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";

    /** 微信手机号快捷验证组件 code 换取手机号接口地址 */
    private static final String PHONE_NUMBER_URL = "https://api.weixin.qq.com/wxa/business/getuserphonenumber";

    /**
     * 手机号快捷验证组件 code 换取用户手机号
     * 注意：该能力要求已认证的非个人主体小程序；未配置 appid 时直接报错，
     * 前端捕获后应降级为手动输入手机号。
     *
     * @param code getPhoneNumber 事件返回的动态令牌
     * @return 用户手机号
     */
    public String getPhoneNumber(String code)
    {
        if (StringUtils.isBlank(appid))
        {
            throw new ServiceException("未配置小程序appid，无法使用微信手机号快捷填充，请手动输入");
        }
        // 1. 获取接口调用凭证 access_token
        String tokenParam = "grant_type=client_credential&appid=" + appid + "&secret=" + secret;
        String tokenResult = HttpUtils.sendGet(TOKEN_URL, tokenParam);
        JSONObject tokenJson = JSON.parseObject(tokenResult);
        if (tokenJson == null || StringUtils.isBlank(tokenJson.getString("access_token")))
        {
            throw new ServiceException("获取access_token失败: "
                    + (tokenJson == null ? "无响应" : tokenJson.getString("errmsg")));
        }
        // 2. code 换取手机号（POST JSON）
        String accessToken = tokenJson.getString("access_token");
        String body = "{\"code\":\"" + code + "\"}";
        String phoneResult = HttpUtils.sendPost(PHONE_NUMBER_URL + "?access_token=" + accessToken, body,
                "application/json");
        JSONObject phoneJson = JSON.parseObject(phoneResult);
        if (phoneJson == null || phoneJson.getIntValue("errcode") != 0)
        {
            throw new ServiceException("获取手机号失败: "
                    + (phoneJson == null ? "无响应" : phoneJson.getString("errmsg")));
        }
        JSONObject phoneInfo = phoneJson.getJSONObject("phone_info");
        String phoneNumber = phoneInfo == null ? null : phoneInfo.getString("purePhoneNumber");
        if (StringUtils.isBlank(phoneNumber))
        {
            throw new ServiceException("获取手机号失败: 响应中无手机号");
        }
        return phoneNumber;
    }
}
