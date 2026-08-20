package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.Camera;
import com.ruoyi.system.domain.Event;
import com.ruoyi.system.mapper.CameraMapper;
import com.ruoyi.system.mapper.EventMapper;
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

    @Autowired
    private EventMapper eventMapper;

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

    /**
     * 批量导入摄像头：按赛事名称解析 eventId，同赛事下编码已存在时按 updateSupport 更新或跳过
     *
     * @param cameraList 摄像头列表
     * @param updateSupport 是否更新已存在的摄像头
     * @return 导入结果信息
     */
    @Override
    public String importCamera(List<Camera> cameraList, boolean updateSupport)
    {
        if (cameraList == null || cameraList.isEmpty())
        {
            throw new ServiceException("导入摄像头数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (Camera camera : cameraList)
        {
            try
            {
                // 按赛事名称解析 eventId
                if (StringUtils.isBlank(camera.getEventName()))
                {
                    throw new ServiceException("赛事名称不能为空");
                }
                Event event = eventMapper.selectEventByName(camera.getEventName().trim());
                if (event == null)
                {
                    throw new ServiceException("赛事「" + camera.getEventName() + "」不存在");
                }
                if (StringUtils.isBlank(camera.getCameraId()))
                {
                    throw new ServiceException("摄像头编码不能为空");
                }
                camera.setEventId(event.getId());
                camera.setEventName(null);
                if (StringUtils.isNull(camera.getStatus()))
                {
                    camera.setStatus(1);
                }
                Camera exist = cameraMapper.selectByEventAndCameraId(camera.getEventId(), camera.getCameraId().trim());
                if (exist != null)
                {
                    if (updateSupport)
                    {
                        camera.setId(exist.getId());
                        camera.setCreateTime(null);
                        cameraMapper.updateCamera(camera);
                        successNum++;
                        successMsg.append("<br/>").append(successNum).append("、摄像头 ").append(camera.getCameraId()).append(" 更新成功");
                    }
                    else
                    {
                        failureNum++;
                        failureMsg.append("<br/>").append(failureNum).append("、摄像头 ").append(camera.getCameraId()).append(" 已存在，跳过");
                    }
                }
                else
                {
                    camera.setCreateTime(DateUtils.getNowDate());
                    cameraMapper.insertCamera(camera);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、摄像头 ").append(camera.getCameraId()).append(" 导入成功");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                failureMsg.append("<br/>").append(failureNum).append("、摄像头 ").append(camera.getCameraId() == null ? "" : camera.getCameraId()).append(" 导入失败：").append(e.getMessage());
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式错误，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
}
