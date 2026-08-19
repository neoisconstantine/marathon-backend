package com.ruoyi.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 摄像头信息对象 camera
 * 
 * @author ruoyi
 */
public class Camera extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 摄像头ID */
    private Long id;

    /** 关联赛事ID */
    private Long eventId;

    /** 摄像头ID/点位编码（如 CP-05KM） */
    private String cameraId;

    /** 摄像头名称（如 5公里计时点） */
    private String name;

    /** 安装位置描述 */
    private String location;

    /** 经度（大屏地图/热力图定位） */
    private BigDecimal lng;

    /** 纬度（大屏地图/热力图定位） */
    private BigDecimal lat;

    /** 状态（0停用 1启用） */
    private Integer status;

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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public BigDecimal getLng()
    {
        return lng;
    }

    public void setLng(BigDecimal lng)
    {
        this.lng = lng;
    }

    public BigDecimal getLat()
    {
        return lat;
    }

    public void setLat(BigDecimal lat)
    {
        this.lat = lat;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("eventId", getEventId())
            .append("cameraId", getCameraId())
            .append("name", getName())
            .append("location", getLocation())
            .append("lng", getLng())
            .append("lat", getLat())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
