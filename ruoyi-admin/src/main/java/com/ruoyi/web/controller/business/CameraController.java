package com.ruoyi.web.controller.business;

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
import com.ruoyi.system.domain.Camera;
import com.ruoyi.system.service.ICameraService;

/**
 * 摄像头管理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/business/camera")
public class CameraController extends BaseController
{
    @Autowired
    private ICameraService cameraService;

    /**
     * 获取摄像头列表
     */
    @PreAuthorize("@ss.hasPermi('business:camera:list')")
    @GetMapping("/list")
    public TableDataInfo list(Camera camera)
    {
        startPage();
        return getDataTable(cameraService.selectCameraList(camera));
    }

    /**
     * 根据摄像头编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:camera:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id)
    {
        return success(cameraService.selectCameraById(id));
    }

    /**
     * 新增摄像头
     */
    @PreAuthorize("@ss.hasPermi('business:camera:add')")
    @Log(title = "摄像头管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody Camera camera)
    {
        return toAjax(cameraService.insertCamera(camera));
    }

    /**
     * 修改摄像头
     */
    @PreAuthorize("@ss.hasPermi('business:camera:edit')")
    @Log(title = "摄像头管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody Camera camera)
    {
        return toAjax(cameraService.updateCamera(camera));
    }

    /**
     * 删除摄像头
     */
    @PreAuthorize("@ss.hasPermi('business:camera:remove')")
    @Log(title = "摄像头管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(cameraService.deleteCameraByIds(ids));
    }
}
