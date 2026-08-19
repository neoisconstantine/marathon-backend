package com.ruoyi.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.system.domain.Camera;

/**
 * 摄像头信息 数据层
 * 
 * @author ruoyi
 */
public interface CameraMapper
{
    /**
     * 通过ID查询摄像头
     * 
     * @param id 摄像头ID
     * @return 摄像头信息
     */
    public Camera selectCameraById(Long id);

    /**
     * 查询摄像头列表
     * 
     * @param camera 摄像头信息
     * @return 摄像头集合
     */
    public List<Camera> selectCameraList(Camera camera);

    /**
     * 通过赛事ID和摄像头编码查询摄像头
     * 
     * @param eventId 赛事ID
     * @param cameraId 摄像头编码
     * @return 摄像头信息
     */
    public Camera selectByEventAndCameraId(@Param("eventId") Long eventId, @Param("cameraId") String cameraId);

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
     * 删除摄像头
     * 
     * @param id 摄像头ID
     * @return 结果
     */
    public int deleteCameraById(Long id);

    /**
     * 批量删除摄像头
     * 
     * @param ids 需要删除的摄像头ID
     * @return 结果
     */
    public int deleteCameraByIds(Long[] ids);
}
