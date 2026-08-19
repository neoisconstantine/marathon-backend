package com.ruoyi.web.controller.api;

import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.domain.ApiResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.WxLoginService;

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

    /**
     * 微信登录（code换取小程序令牌）
     */
    @PostMapping("/wx-login")
    public ApiResult wxLogin(@RequestBody Map<String, String> body)
    {
        String code = body.get("code");
        if (StringUtils.isBlank(code))
        {
            return ApiResult.error("code不能为空");
        }
        String token = wxLoginService.wxLogin(code);
        return ApiResult.success("ok", Collections.singletonMap("token", token));
    }
}
