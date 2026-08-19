package com.ruoyi.system.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 点位到达时间（成绩计算用）
 *
 * @author ruoyi
 */
public class PassArrivalVo
{
    /** 号码牌 */
    private String bib;

    /** 摄像头ID/点位编码 */
    private String cameraId;

    /** 最早到达时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date arriveTime;

    public String getBib()
    {
        return bib;
    }

    public void setBib(String bib)
    {
        this.bib = bib;
    }

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
}
