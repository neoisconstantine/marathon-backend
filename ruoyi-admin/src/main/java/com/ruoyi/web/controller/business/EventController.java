package com.ruoyi.web.controller.business;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.Event;
import com.ruoyi.system.service.IEventService;

/**
 * 赛事管理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/business/event")
public class EventController extends BaseController
{
    @Autowired
    private IEventService eventService;

    /**
     * 获取赛事列表
     */
    @PreAuthorize("@ss.hasPermi('business:event:list')")
    @GetMapping("/list")
    public TableDataInfo list(Event event)
    {
        startPage();
        List<Event> list = eventService.selectEventList(event);
        return getDataTable(list);
    }

    /**
     * 根据赛事编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:event:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(eventService.selectEventById(id));
    }

    /**
     * 新增赛事
     */
    @PreAuthorize("@ss.hasPermi('business:event:add')")
    @Log(title = "赛事管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody Event event)
    {
        return toAjax(eventService.insertEvent(event));
    }

    /**
     * 修改赛事
     */
    @PreAuthorize("@ss.hasPermi('business:event:edit')")
    @Log(title = "赛事管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody Event event)
    {
        return toAjax(eventService.updateEvent(event));
    }

    /**
     * 删除赛事
     */
    @PreAuthorize("@ss.hasPermi('business:event:remove')")
    @Log(title = "赛事管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(eventService.deleteEventByIds(ids));
    }

    /**
     * 修改赛事状态
     */
    @PreAuthorize("@ss.hasPermi('business:event:edit')")
    @Log(title = "赛事管理", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}/status")
    public AjaxResult changeStatus(@PathVariable Long id, Integer status)
    {
        return toAjax(eventService.changeEventStatus(id, status));
    }
}
