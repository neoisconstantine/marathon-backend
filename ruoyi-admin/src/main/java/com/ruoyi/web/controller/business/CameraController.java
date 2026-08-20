package com.ruoyi.web.controller.business;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
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

    /**
     * 导出摄像头列表
     */
    @Log(title = "摄像头管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('business:camera:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, Camera camera)
    {
        List<Camera> list = cameraService.selectCameraList(camera);
        ExcelUtil<Camera> util = new ExcelUtil<Camera>(Camera.class);
        util.exportExcel(response, list, "摄像头数据");
    }

    /**
     * 导入摄像头数据
     */
    @Log(title = "摄像头管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('business:camera:import')")
    @PostMapping("/import")
    public AjaxResult importData(MultipartFile file, boolean updateSupport)
    {
        try
        {
            ExcelUtil<Camera> util = new ExcelUtil<Camera>(Camera.class);
            List<Camera> cameraList = util.importExcel(file.getInputStream());
            String message = cameraService.importCamera(cameraList, updateSupport);
            return success(message);
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }

    /**
     * 下载摄像头导入模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<Camera> util = new ExcelUtil<Camera>(Camera.class);
        util.importTemplateExcel(response, "摄像头数据");
    }
}
