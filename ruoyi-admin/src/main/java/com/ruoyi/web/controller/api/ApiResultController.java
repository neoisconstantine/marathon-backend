package com.ruoyi.web.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.domain.ApiResult;
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
     * 我的赛事成绩列表
     */
    @GetMapping("/my")
    public ApiResult my()
    {
        Long personId = WxSecurityUtils.getPersonId();
        return ApiResult.success(resultService.selectMyResults(personId));
    }

    /**
     * 成绩详情
     */
    @GetMapping("/detail")
    public ApiResult detail(@RequestParam Long id)
    {
        Long personId = WxSecurityUtils.getPersonId();
        Result result = resultService.selectResultById(id);
        if (result == null || !personId.equals(result.getPersonId()))
        {
            return ApiResult.error("成绩不存在");
        }
        return ApiResult.success(result);
    }
}
