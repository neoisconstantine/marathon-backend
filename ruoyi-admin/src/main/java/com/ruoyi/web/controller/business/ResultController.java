package com.ruoyi.web.controller.business;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.Result;
import com.ruoyi.system.service.IResultService;

/**
 * 成绩管理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/business/result")
public class ResultController extends BaseController
{
    @Autowired
    private IResultService resultService;

    /**
     * 获取成绩列表
     */
    @PreAuthorize("@ss.hasPermi('business:result:list')")
    @GetMapping("/list")
    public TableDataInfo list(Result result)
    {
        startPage();
        List<Result> list = resultService.selectResultList(result);
        return getDataTable(list);
    }

    /**
     * 根据成绩编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:result:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(resultService.selectResultById(id));
    }

    /**
     * 修改成绩
     */
    @PreAuthorize("@ss.hasPermi('business:result:edit')")
    @Log(title = "成绩管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody Result result)
    {
        return toAjax(resultService.updateResult(result));
    }

    /**
     * 确认成绩
     */
    @PreAuthorize("@ss.hasPermi('business:result:confirm')")
    @Log(title = "成绩管理", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}/confirm")
    public AjaxResult confirm(@PathVariable Long id, Integer status)
    {
        return toAjax(resultService.confirmResult(id, status));
    }

    /**
     * 删除成绩
     */
    @PreAuthorize("@ss.hasPermi('business:result:remove')")
    @Log(title = "成绩管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(resultService.deleteResultByIds(ids));
    }
}
