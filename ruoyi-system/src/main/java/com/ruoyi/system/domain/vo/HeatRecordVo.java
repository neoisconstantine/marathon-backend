package com.ruoyi.system.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 热力图点位到达记录（按摄像头聚合的最早到达时间）
 *
 * @author ruoyi
 */
public class HeatRecordVo
{
    /** 摄像头ID/点位编码 */
    private String cameraId;

    /** 参赛号码布 */
    private String bib;

    /** 参赛用户ID */
    private String personId;

    /** 参赛用户姓名 */
    private String personName;

    /** 最早到达时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date arriveTime;

    public String getCameraId()
    {
        return cameraId;
    }

    public void setCameraId(String cameraId)
    {
        this.cameraId = cameraId;
    }

    public String getBib()
    {
        return bib;
    }

    public void setBib(String bib)
    {
        this.bib = bib;
    }

    public String getPersonId()
    {
        return personId;
    }

    public void setPersonId(String personId)
    {
        this.personId = personId;
    }

    public String getPersonName()
    {
        return personName;
    }

    public void setPersonName(String personName)
    {
        this.personName = personName;
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
