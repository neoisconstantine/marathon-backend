package com.ruoyi.web.controller.api;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.RateLimiter;
import com.ruoyi.common.core.domain.ApiResult;
import com.ruoyi.common.enums.LimitType;
import com.ruoyi.system.domain.Camera;
import com.ruoyi.system.service.ICameraService;

/**
 * 小程序摄像头点位 控制器
 * 用途：赛道沿线计时摄像头 GPS 点位下发，小程序端按点位顺序连成赛道路线轨迹
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/api/camera")
public class ApiCameraController
{
    @Autowired
    private ICameraService cameraService;

    /**
     * 摄像头点位列表（按 camera_id 升序，即赛道里程顺序）
     *
     * @param eventId 赛事ID（必填，点位按赛事隔离）
     */
    @RateLimiter(time = 1, count = 20, limitType = LimitType.IP)
    @GetMapping("/list")
    public ApiResult list(@RequestParam Long eventId)
    {
        if (eventId == null)
        {
            return ApiResult.error("eventId不能为空");
        }
        Camera query = new Camera();
        query.setEventId(eventId);
        // 仅下发启用状态的点位
        query.setStatus(1);
        List<Camera> list = cameraService.selectCameraList(query);
        return ApiResult.success(list);
    }
}
