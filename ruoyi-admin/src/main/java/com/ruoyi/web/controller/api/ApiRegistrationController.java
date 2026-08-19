package com.ruoyi.web.controller.api;

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
import com.ruoyi.system.service.IRegistrationService;

/**
 * 小程序报名 控制器
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/api/registration")
public class ApiRegistrationController
{
    @Autowired
    private IRegistrationService registrationService;

    /**
     * 创建报名
     */
    @PostMapping("/create")
    public ApiResult create(@RequestBody Map<String, String> body)
    {
        String eventIdStr = body.get("eventId");
        if (StringUtils.isBlank(eventIdStr))
        {
            return ApiResult.error("eventId不能为空");
        }
        Long eventId;
        try
        {
            eventId = Long.valueOf(eventIdStr);
        }
        catch (NumberFormatException e)
        {
            return ApiResult.error("eventId格式错误");
        }
        Long personId = WxSecurityUtils.getPersonId();
        int rows = registrationService.createRegistration(personId, eventId);
        return rows > 0 ? ApiResult.success("报名成功", null) : ApiResult.error("报名失败");
    }

    /**
     * 我的报名记录
     */
    @GetMapping("/my")
    public ApiResult my()
    {
        Long personId = WxSecurityUtils.getPersonId();
        return ApiResult.success(registrationService.selectMyRegistrations(personId));
    }
}
