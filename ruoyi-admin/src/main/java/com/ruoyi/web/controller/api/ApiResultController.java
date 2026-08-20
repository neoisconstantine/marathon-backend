package com.ruoyi.web.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.RateLimiter;
import com.ruoyi.common.core.domain.ApiResult;
import com.ruoyi.common.enums.LimitType;
import com.ruoyi.common.utils.WxSecurityUtils;
import com.ruoyi.system.domain.Result;
import com.ruoyi.system.service.IResultService;

/**
 * 小程序成绩 控制器
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/api/result")
public class ApiResultController
{
    @Autowired
    private IResultService resultService;

    /**
     * 某赛事的成绩列表（公开：成绩查询页展示已结束赛事的所有选手成绩，按排名升序）
     */
    @RateLimiter(time = 1, count = 20, limitType = LimitType.IP)
    @GetMapping("/event")
    public ApiResult event(@RequestParam Long eventId)
    {
        Result query = new Result();
        query.setEventId(eventId);
        return ApiResult.success(resultService.selectResultList(query));
    }

    /**
     * 我的赛事成绩列表
     */
    @RateLimiter(time = 1, count = 20, limitType = LimitType.IP)
    @GetMapping("/my")
    public ApiResult my()
    {
        Long personId = WxSecurityUtils.getPersonId();
        return ApiResult.success(resultService.selectMyResults(personId));
    }

    /**
     * 成绩详情（含分段明细）
     */
    @RateLimiter(time = 1, count = 20, limitType = LimitType.IP)
    @GetMapping("/detail")
    public ApiResult detail(@RequestParam Long id)
    {
        Long personId = WxSecurityUtils.getPersonId();
        Result result = resultService.selectResultDetail(id);
        if (result == null || !personId.equals(result.getPersonId()))
        {
            return ApiResult.error("成绩不存在");
        }
        return ApiResult.success(result);
    }
}
