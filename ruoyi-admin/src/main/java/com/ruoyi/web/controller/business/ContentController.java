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
import com.ruoyi.system.domain.Content;
import com.ruoyi.system.service.IContentService;

/**
 * 资讯管理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/business/content")
public class ContentController extends BaseController
{
    @Autowired
    private IContentService contentService;

    /**
     * 获取资讯列表
     */
    @PreAuthorize("@ss.hasPermi('business:content:list')")
    @GetMapping("/list")
    public TableDataInfo list(Content content)
    {
        startPage();
        List<Content> list = contentService.selectContentList(content);
        return getDataTable(list);
    }

    /**
     * 根据资讯编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:content:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(contentService.selectContentById(id));
    }

    /**
     * 新增资讯
     */
    @PreAuthorize("@ss.hasPermi('business:content:add')")
    @Log(title = "资讯管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody Content content)
    {
        return toAjax(contentService.insertContent(content));
    }

    /**
     * 修改资讯
     */
    @PreAuthorize("@ss.hasPermi('business:content:edit')")
    @Log(title = "资讯管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody Content content)
    {
        return toAjax(contentService.updateContent(content));
    }

    /**
     * 删除资讯
     */
    @PreAuthorize("@ss.hasPermi('business:content:remove')")
    @Log(title = "资讯管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(contentService.deleteContentByIds(ids));
    }
}
