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
        // status 为空：小程序端过滤未发布赛事（status=0），仅展示报名中/进行中/已结束的赛事。
        // 用 -1 哨兵在 SQL 层过滤（WHERE e.status != 0），保证 PageHelper 分页 total 与每页数量正确；
        // 若在内存 removeIf 会因 total 含未发布导致分页永远加载不完。
        Event query = event;
        if (event.getStatus() == null)
        {
            query = new Event();
            org.springframework.beans.BeanUtils.copyProperties(event, query);
            query.setStatus(-1);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Event> list = eventService.selectEventList(query);
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
