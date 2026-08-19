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
import com.ruoyi.system.domain.Alarm;
import com.ruoyi.system.service.IAlarmService;

/**
 * 报警管理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/business/alarm")
public class AlarmController extends BaseController
{
    @Autowired
    private IAlarmService alarmService;

    /**
     * 获取报警列表
     */
    @PreAuthorize("@ss.hasPermi('business:alarm:list')")
    @GetMapping("/list")
    public TableDataInfo list(Alarm alarm)
    {
        startPage();
        List<Alarm> list = alarmService.selectAlarmList(alarm);
        return getDataTable(list);
    }

    /**
     * 根据报警编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:alarm:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(alarmService.selectAlarmById(id));
    }

    /**
     * 新增报警
     */
    @PreAuthorize("@ss.hasPermi('business:alarm:add')")
    @Log(title = "报警管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody Alarm alarm)
    {
        return toAjax(alarmService.insertAlarm(alarm));
    }

    /**
     * 修改报警
     */
    @PreAuthorize("@ss.hasPermi('business:alarm:edit')")
    @Log(title = "报警管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody Alarm alarm)
    {
        return toAjax(alarmService.updateAlarm(alarm));
    }

    /**
     * 删除报警
     */
    @PreAuthorize("@ss.hasPermi('business:alarm:remove')")
    @Log(title = "报警管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(alarmService.deleteAlarmByIds(ids));
    }
}