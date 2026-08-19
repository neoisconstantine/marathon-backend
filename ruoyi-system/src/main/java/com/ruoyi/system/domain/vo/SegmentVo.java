package com.ruoyi.system.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 成绩分段明细
 *
 * @author ruoyi
 */
public class SegmentVo
{
    /** 摄像头ID/点位编码 */
    private String cameraId;

    /** 到达该点位时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date arriveTime;

    /** 本分段用时（毫秒；首段为null） */
    private Long segmentDuration;

    /** 本分段用时（HH:mm:ss.SS 格式；首段为null） */
    private String segmentDurationText;

    public String getCameraId()
    {
        return cameraId;
    }

    public void setCameraId(String cameraId)
    {
        this.cameraId = cameraId;
    }

    public Date getArriveTime()
    {
        return arriveTime;
    }

    public void setArriveTime(Date arriveTime)
    {
        this.arriveTime = arriveTime;
    }

    public Long getSegmentDuration()
    {
        return segmentDuration;
    }

    public void setSegmentDuration(Long segmentDuration)
    {
        this.segmentDuration = segmentDuration;
    }

    public String getSegmentDurationText()
    {
        return segmentDurationText;
    }

    public void setSegmentDurationText(String segmentDurationText)
    {
        this.segmentDurationText = segmentDurationText;
    }
}
