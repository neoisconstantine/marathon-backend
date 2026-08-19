package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 计时打卡记录对象 pass_record
 * 
 * @author ruoyi
 */
public class PassRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 打卡记录ID */
    private Long id;

    /** 赛事ID */
    private Long eventId;

    /** 计时点设备编号 */
    private String cameraId;

    /** 参赛号码布 */
    private String bib;

    /** 参赛用户ID */
    private String personId;

    /** 打卡时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date passTime;

    /** 最初到达时间（该号码牌首次经过该摄像头的时刻，用于分段用时计算） */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date firstArriveTime;

    /** 通过速度 */
    private BigDecimal speed;

    /** 计时点经度 */
    private BigDecimal cameraLng;

    /** 计时点纬度 */
    private BigDecimal cameraLat;

    /** 扩展信息 */
    private String extra;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getEventId()
    {
        return eventId;
    }

    public void setEventId(Long eventId)
    {
        this.eventId = eventId;
    }

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

    public Date getPassTime()
    {
        return passTime;
    }

    public void setPassTime(Date passTime)
    {
        this.passTime = passTime;
    }

    public Date getFirstArriveTime()
    {
        return firstArriveTime;
    }

    public void setFirstArriveTime(Date firstArriveTime)
    {
        this.firstArriveTime = firstArriveTime;
    }

    public BigDecimal getSpeed()
    {
        return speed;
    }

    public void setSpeed(BigDecimal speed)
    {
        this.speed = speed;
    }

    public BigDecimal getCameraLng()
    {
        return cameraLng;
    }

    public void setCameraLng(BigDecimal cameraLng)
    {
        this.cameraLng = cameraLng;
    }

    public BigDecimal getCameraLat()
    {
        return cameraLat;
    }

    public void setCameraLat(BigDecimal cameraLat)
    {
        this.cameraLat = cameraLat;
    }

    public String getExtra()
    {
        return extra;
    }

    public void setExtra(String extra)
    {
        this.extra = extra;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("eventId", getEventId())
            .append("cameraId", getCameraId())
            .append("bib", getBib())
            .append("personId", getPersonId())
            .append("passTime", getPassTime())
            .append("firstArriveTime", getFirstArriveTime())
            .append("speed", getSpeed())
            .append("cameraLng", getCameraLng())
            .append("cameraLat", getCameraLat())
            .append("extra", getExtra())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
