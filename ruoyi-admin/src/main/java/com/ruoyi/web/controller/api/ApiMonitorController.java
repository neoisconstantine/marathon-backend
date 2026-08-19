package com.ruoyi.web.controller.api;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.domain.ApiResult;
import com.ruoyi.system.domain.Camera;
import com.ruoyi.system.domain.vo.HeatRecordVo;
import com.ruoyi.system.mapper.CameraMapper;
import com.ruoyi.system.mapper.PassRecordMapper;

/**
 * 大屏监控数据 控制器
 * 用途：大屏展示聚合接口（热力图等），与业务 CRUD 接口分离，便于独立维护
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/api/monitor")
public class ApiMonitorController
{
    /** 热力图 tooltip 展示的到达用户数上限 */
    private static final int USERS_LIMIT = 5;

    @Autowired
    private CameraMapper cameraMapper;

    @Autowired
    private PassRecordMapper passRecordMapper;

    /**
     * 人流热力图：
     * 返回该赛事全部摄像头点位（真实 GPS）+ 各点位到达人数（pass_record 按摄像头分组统计），
     * 无到达记录的点位 count=0（前端不渲染热力）。
     *
     * @param eventId 赛事ID（必填）
     */
    @GetMapping("/heatmap")
    public ApiResult heatmap(@RequestParam Long eventId)
    {
        if (eventId == null)
        {
            return ApiResult.error("eventId不能为空");
        }

        // 1. 摄像头点位（仅启用）
        Camera query = new Camera();
        query.setEventId(eventId);
        query.setStatus(1);
        List<Camera> cameras = cameraMapper.selectCameraList(query);
        // 按 id 升序（点位编码 START→…→FINISH 顺序），保证赛道折线方向正确
        cameras.sort(Comparator.comparing(Camera::getId));

        // 2. 各点位最早到达记录（含用户姓名）
        List<HeatRecordVo> records = passRecordMapper.selectHeatRecordsByEvent(eventId);

        // 3. 按 cameraId 聚合：count + 到达用户列表
        Map<String, List<HeatRecordVo>> grouped = new LinkedHashMap<>();
        for (HeatRecordVo r : records)
        {
            grouped.computeIfAbsent(r.getCameraId(), k -> new ArrayList<>()).add(r);
        }

        List<Map<String, Object>> points = new ArrayList<>();
        int maxCount = 0;
        for (Camera cam : cameras)
        {
            List<HeatRecordVo> users = grouped.get(cam.getCameraId());
            int count = (users == null) ? 0 : users.size();
            maxCount = Math.max(maxCount, count);

            Map<String, Object> point = new LinkedHashMap<>();
            point.put("cameraId", cam.getCameraId());
            point.put("name", cam.getName());
            point.put("lng", cam.getLng());
            point.put("lat", cam.getLat());
            point.put("count", count);
            points.add(point);
        }
        // 归一化热度（count / maxCount），无记录为 0
        for (Map<String, Object> p : points)
        {
            int count = (Integer) p.get("count");
            p.put("heat", maxCount > 0 ? Math.round(count * 1000.0 / maxCount) / 1000.0 : 0);

            List<HeatRecordVo> users = grouped.get(p.get("cameraId"));
            List<Map<String, Object>> userList = new ArrayList<>();
            if (users != null)
            {
                for (int i = 0; i < Math.min(users.size(), USERS_LIMIT); i++)
                {
                    HeatRecordVo u = users.get(i);
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("bib", u.getBib());
                    m.put("name", u.getPersonName());
                    m.put("passTime", u.getArriveTime());
                    userList.add(m);
                }
            }
            p.put("users", userList);
        }

        // 4. 赛道折线（按点位顺序连成轨迹）
        List<double[]> route = new ArrayList<>();
        for (Camera cam : cameras)
        {
            if (cam.getLng() != null && cam.getLat() != null)
            {
                route.add(new double[] { cam.getLng().doubleValue(), cam.getLat().doubleValue() });
            }
        }

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("route", route);
        data.put("points", points);
        return ApiResult.success(data);
    }
}
