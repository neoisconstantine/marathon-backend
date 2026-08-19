package com.ruoyi.web.controller.api;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.ApiResult;
import com.ruoyi.system.domain.Event;
import com.ruoyi.system.service.IEventService;

/**
 * 小程序赛事 控制器
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/api/event")
public class ApiEventController
{
    @Autowired
    private IEventService eventService;

    /**
     * 赛事列表（分页）
     */
    @GetMapping("/list")
    public ApiResult list(Event event, @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<Event> list = eventService.selectEventList(event);
        if (event.getStatus() == null)
        {
            // 小程序端过滤未发布赛事（状态0），仅展示报名中/进行中/已结束的赛事
            list.removeIf(e -> e.getStatus() != null && e.getStatus() == 0);
        }
        return ApiResult.success("ok", Map.of("list", list, "total", new PageInfo<>(list).getTotal()));
    }

    /**
     * 赛事详情
     */
    @GetMapping("/detail")
    public ApiResult detail(@RequestParam Long id)
    {
        Event event = eventService.selectEventById(id);
        if (event == null)
        {
            return ApiResult.error("赛事不存在");
        }
        return ApiResult.success(event);
    }
}
