package com.ruoyi.web.controller.api;

import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.RateLimiter;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.domain.ApiResult;
import com.ruoyi.common.enums.LimitType;
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
    @RateLimiter(time = 10, count = 10, limitType = LimitType.IP)
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
    @RateLimiter(time = 60, count = 10, limitType = LimitType.IP)
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
     * 完善用户信息：绑定手机号/设置昵称姓名（首次登录引导弹窗提交）
     * 入参 { phone, name } 均可选，至少传一个；非空字段才更新（动态SQL只更新非空列）
     * name：昵称输入框(type=nickname)带入的微信昵称或手动输入的姓名；
     *       后续报名时若用户填写了真实姓名，会经 createRegistration 回填覆盖
     */
    @RateLimiter(time = 10, count = 5, limitType = LimitType.IP)
    @RepeatSubmit(interval = 3000, message = "请勿重复提交绑定请求")
    @PostMapping("/bind-phone")
    public ApiResult bindPhone(@RequestBody Map<String, String> body)
    {
        String phone = body.get("phone");
        String name = body.get("name");
        if (StringUtils.isBlank(phone) && StringUtils.isBlank(name))
        {
            return ApiResult.error("请至少填写手机号或昵称");
        }
        if (StringUtils.isNotBlank(phone) && !phone.matches("1\\d{10}"))
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
        // 动态SQL只更新非null字段：blank值统一置null避免把已有数据覆盖成空串
        person.setPhone(StringUtils.isBlank(phone) ? null : phone);
        person.setName(StringUtils.isBlank(name) ? null : name.trim());
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
