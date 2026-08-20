package com.ruoyi.web.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.RateLimiter;
import com.ruoyi.common.core.domain.ApiResult;
import com.ruoyi.common.enums.LimitType;
import com.ruoyi.system.service.IContentService;

/**
 * 小程序内容（轮播图/公告） 控制器
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/api/content")
public class ApiContentController
{
    @Autowired
    private IContentService contentService;

    /**
     * 轮播图列表
     */
    @RateLimiter(time = 1, count = 20, limitType = LimitType.IP)
    @GetMapping("/banner")
    public ApiResult banner()
    {
        return ApiResult.success(contentService.selectBannerList());
    }

    /**
     * 公告列表
     */
    @RateLimiter(time = 1, count = 20, limitType = LimitType.IP)
    @GetMapping("/notice")
    public ApiResult notice()
    {
        return ApiResult.success(contentService.selectNoticeList());
    }
}
