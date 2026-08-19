package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.Camera;
import com.ruoyi.system.mapper.CameraMapper;
import com.ruoyi.system.service.ICameraService;

/**
 * 摄像头管理 服务层实现
 *
 * @author ruoyi
 */
@Service("cameraService")
public class CameraServiceImpl implements ICameraService
{
    @Autowired
    private CameraMapper cameraMapper;

    /**
     * 查询摄像头列表
     *
     * @param camera 摄像头信息
     * @return 摄像头集合
     */
    @Override
    public List<Camera> selectCameraList(Camera camera)
    {
        return cameraMapper.selectCameraList(camera);
    }

    /**
     * 查询摄像头信息
     *
     * @param id 摄像头ID
     * @return 摄像头信息
     */
    @Override
    public Camera selectCameraById(Long id)
    {
        return cameraMapper.selectCameraById(id);
    }

    /**
     * 新增摄像头（同赛事下编码唯一校验）
     *
     * @param camera 摄像头信息
     * @return 结果
     */
    @Override
    public int insertCamera(Camera camera)
    {
        if (StringUtils.isNull(camera.getStatus()))
        {
            camera.setStatus(1); // 1启用
        }
        camera.setCreateTime(DateUtils.getNowDate());
        Camera exist = cameraMapper.selectByEventAndCameraId(camera.getEventId(), camera.getCameraId());
        if (StringUtils.isNotNull(exist))
        {
            throw new ServiceException("该赛事下摄像头编码已存在");
        }
        return cameraMapper.insertCamera(camera);
    }

    /**
     * 修改摄像头
     *
     * @param camera 摄像头信息
     * @return 结果
     */
    @Override
    public int updateCamera(Camera camera)
    {
        camera.setUpdateTime(DateUtils.getNowDate());
        return cameraMapper.updateCamera(camera);
    }

    /**
     * 批量删除摄像头
     *
     * @param ids 需要删除的摄像头ID
     * @return 结果
     */
    @Override
    public int deleteCameraByIds(Long[] ids)
    {
        return cameraMapper.deleteCameraByIds(ids);
    }
}
