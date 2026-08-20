package com.ruoyi.web.controller.api;

import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.domain.ApiResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.WxSecurityUtils;
import com.ruoyi.framework.web.service.WxLoginService;
import com.ruoyi.system.domain.Person;
import com.ruoyi.system.service.IPersonService;

/**
 * 小程序认证 控制器
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/api/auth")
public class ApiAuthController
{
    @Autowired
    private WxLoginService wxLoginService;

    @Autowired
    private IPersonService personService;

    /**
     * 微信登录（code换取小程序令牌）
     * 返回 { token, isNewUser }：isNewUser=true 表示首次登录自动注册的新用户
     */
    @PostMapping("/wx-login")
    public ApiResult wxLogin(@RequestBody Map<String, String> body)
    {
        String code = body.get("code");
        if (StringUtils.isBlank(code))
        {
            return ApiResult.error("code不能为空");
        }
        return ApiResult.success("ok", wxLoginService.wxLogin(code));
    }

    /**
     * 手机号快捷验证组件 code 换取用户手机号
     * （需已认证小程序并配置 wx.appid；未配置时返回明确错误，前端降级为手动输入）
     */
    @PostMapping("/phone")
    public ApiResult phone(@RequestBody Map<String, String> body)
    {
        String code = body.get("code");
        if (StringUtils.isBlank(code))
        {
            return ApiResult.error("code不能为空");
        }
        String phone = wxLoginService.getPhoneNumber(code);
        return ApiResult.success("ok", Collections.singletonMap("phone", phone));
    }

    /**
     * 绑定手机号：将授权获取到的手机号回填到当前登录用户（首次登录引导授权后调用）
     */
    @PostMapping("/bind-phone")
    public ApiResult bindPhone(@RequestBody Map<String, String> body)
    {
        String phone = body.get("phone");
        if (StringUtils.isBlank(phone) || !phone.matches("1\\d{10}"))
        {
            return ApiResult.error("手机号格式不正确");
        }
        Long personId = WxSecurityUtils.getPersonId();
        if (personId == null)
        {
            return ApiResult.error("未登录");
        }
        Person person = new Person();
        person.setId(personId);
        person.setPhone(phone);
        personService.updatePerson(person);
        return ApiResult.success("ok", null);
    }

    /**
     * 当前登录用户信息（需 wx-token，用于"我的"页展示登录态）
     */
    @GetMapping("/me")
    public ApiResult me()
    {
        Long personId = WxSecurityUtils.getPersonId();
        Person person = personService.selectPersonById(personId);
        if (person == null)
        {
            return ApiResult.error("用户不存在");
        }
        Map<String, Object> data = new java.util.HashMap<>();
        data.put("id", person.getId());
        data.put("name", person.getName());
        data.put("phone", person.getPhone());
        return ApiResult.success(data);
    }
}
