package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Camera;

/**
 * 摄像头管理 服务层
 *
 * @author ruoyi
 */
public interface ICameraService
{
    /**
     * 查询摄像头列表
     *
     * @param camera 摄像头信息
     * @return 摄像头集合
     */
    public List<Camera> selectCameraList(Camera camera);

    /**
     * 查询摄像头信息
     *
     * @param id 摄像头ID
     * @return 摄像头信息
     */
    public Camera selectCameraById(Long id);

    /**
     * 新增摄像头
     *
     * @param camera 摄像头信息
     * @return 结果
     */
    public int insertCamera(Camera camera);

    /**
     * 修改摄像头
     *
     * @param camera 摄像头信息
     * @return 结果
     */
    public int updateCamera(Camera camera);

    /**
     * 批量删除摄像头
     *
     * @param ids 需要删除的摄像头ID
     * @return 结果
     */
    public int deleteCameraByIds(Long[] ids);
}
