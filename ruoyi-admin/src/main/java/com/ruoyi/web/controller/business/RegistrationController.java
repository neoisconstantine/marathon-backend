package com.ruoyi.web.controller.business;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.Registration;
import com.ruoyi.system.service.IRegistrationService;

/**
 * 报名管理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/business/registration")
public class RegistrationController extends BaseController
{
    @Autowired
    private IRegistrationService registrationService;

    /**
     * 获取报名列表
     */
    @PreAuthorize("@ss.hasPermi('business:registration:list')")
    @GetMapping("/list")
    public TableDataInfo list(Registration registration)
    {
        startPage();
        List<Registration> list = registrationService.selectRegistrationList(registration);
        return getDataTable(list);
    }

    /**
     * 根据报名编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:registration:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(registrationService.selectRegistrationById(id));
    }

    /**
     * 新增报名（管理端直接录入）
     */
    @PreAuthorize("@ss.hasPermi('business:registration:add')")
    @Log(title = "报名管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Registration registration)
    {
        return toAjax(registrationService.insertRegistration(registration));
    }

    /**
     * 修改报名（管理端编辑：号码布/状态等）
     */
    @PreAuthorize("@ss.hasPermi('business:registration:edit')")
    @Log(title = "报名管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Registration registration)
    {
        return toAjax(registrationService.updateRegistration(registration));
    }

    /**
     * 审核报名
     */
    @PreAuthorize("@ss.hasPermi('business:registration:review')")
    @Log(title = "报名管理", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}/review")
    public AjaxResult review(@PathVariable Long id, Integer status)
    {
        return toAjax(registrationService.reviewRegistration(id, status));
    }

    /**
     * 报名退款
     */
    @PreAuthorize("@ss.hasPermi('business:registration:refund')")
    @Log(title = "报名管理", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}/refund")
    public AjaxResult refund(@PathVariable Long id)
    {
        return toAjax(registrationService.refundRegistration(id));
    }

    /**
     * 导出报名名单
     */
    @Log(title = "报名管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('business:registration:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, Registration registration)
    {
        List<Registration> list = registrationService.selectRegistrationList(registration);
        ExcelUtil<Registration> util = new ExcelUtil<Registration>(Registration.class);
        util.exportExcel(response, list, "报名名单");
    }

    /**
     * 删除报名记录
     */
    @PreAuthorize("@ss.hasPermi('business:registration:remove')")
    @Log(title = "报名管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(registrationService.deleteRegistrationByIds(ids));
    }
}
