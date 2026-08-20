package com.ruoyi.web.controller.api;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.RateLimiter;
import com.ruoyi.common.core.domain.ApiResult;
import com.ruoyi.common.enums.LimitType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.PassRecord;
import com.ruoyi.system.mapper.PassRecordMapper;

/**
 * 外部对接层 控制器（接收摄像头点位通过记录）
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/api/integration")
public class ApiIntegrationController
{
    @Autowired
    private PassRecordMapper passRecordMapper;

    /**
     * 单条点位通过记录
     * TODO: 后续增加 API Key 鉴权（管理端生成、可轮换）
     */
    @RateLimiter(time = 1, count = 50, limitType = LimitType.IP)
    @PostMapping("/pass")
    public ApiResult pass(@RequestBody PassRecord passRecord)
    {
        // 基础校验
        if (StringUtils.isBlank(passRecord.getBib()) || StringUtils.isBlank(passRecord.getCameraId()) || StringUtils.isNull(passRecord.getPassTime()))
        {
            return ApiResult.error("bib/camera_id/pass_time 不能为空");
        }
        if (StringUtils.isNull(passRecord.getEventId()))
        {
            return ApiResult.error("event_id 不能为空");
        }
        // first_arrive_time 默认等于 pass_time（首次到达时间，若乱序推送由成绩计算环节用 min(pass_time) 兜底）
        if (StringUtils.isNull(passRecord.getFirstArriveTime()))
        {
            passRecord.setFirstArriveTime(passRecord.getPassTime());
        }
        try
        {
            passRecordMapper.insertPassRecord(passRecord);
        }
        catch (DuplicateKeyException e)
        {
            // (camera_id, bib, pass_time) 唯一索引兜底，重复推送返回幂等成功
            return ApiResult.success("ok", Collections.singletonMap("idempotent", true));
        }
        return ApiResult.success("ok", Collections.singletonMap("id", passRecord.getId()));
    }

    /**
     * 批量推送点位通过记录（≤1000 条/次）
     * TODO: 后续增加 API Key 鉴权
     */
    @RateLimiter(time = 1, count = 50, limitType = LimitType.IP)
    @PostMapping("/pass/batch")
    public ApiResult passBatch(@RequestBody List<PassRecord> list)
    {
        if (StringUtils.isEmpty(list))
        {
            return ApiResult.error("记录列表不能为空");
        }
        if (list.size() > 1000)
        {
            return ApiResult.error("单次批量不超过1000条");
        }
        // 为每条记录填充 first_arrive_time 默认值
        for (PassRecord item : list)
        {
            if (StringUtils.isNull(item.getFirstArriveTime()) && StringUtils.isNotNull(item.getPassTime()))
            {
                item.setFirstArriveTime(item.getPassTime());
            }
        }
        try
        {
            passRecordMapper.batchInsertPassRecord(list);
        }
        catch (DuplicateKeyException e)
        {
            // 批量插入遇唯一索引冲突，回退到逐条插入（跳过重复）
            int success = 0, skip = 0;
            for (PassRecord item : list)
            {
                try
                {
                    passRecordMapper.insertPassRecord(item);
                    success++;
                }
                catch (DuplicateKeyException ex)
                {
                    skip++;
                }
            }
            return ApiResult.success("ok", Map.of("success", success, "skip", skip));
        }
        return ApiResult.success("ok", Map.of("success", list.size()));
    }
}
