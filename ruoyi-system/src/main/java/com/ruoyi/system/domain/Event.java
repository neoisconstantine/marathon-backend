package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 赛事对象 event
 * 
 * @author ruoyi
 */
public class Event extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 赛事ID */
    private Long id;

    /** 赛事名称 */
    private String name;

    /** 比赛地点 */
    private String location;

    /** 比赛开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 报名开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signupStart;

    /** 报名截止时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signupEnd;

    /** 报名总名额 */
    private Integer totalQuota;

    /** 已报名人数 */
    private Integer registered;

    /** 报名开关（0关闭 1开启） */
    private Integer signupOpen;

    /** 赛事状态（0未发布 1报名中 2进行中 3已结束） */
    private Integer status;

    /** 有效报名人数（报名表实时统计，排除已退赛；非 event.registered 冗余字段） */
    private Long registrationCount;

    /** 封面图地址 */
    private String coverUrl;

    /** 赛事介绍 */
    private String intro;

    /** 赛事里程（公里，如 42.195；非必填） */
    private BigDecimal mileage;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
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

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getSignupStart()
    {
        return signupStart;
    }

    public void setSignupStart(Date signupStart)
    {
        this.signupStart = signupStart;
    }

    public Date getSignupEnd()
    {
        return signupEnd;
    }

    public void setSignupEnd(Date signupEnd)
    {
        this.signupEnd = signupEnd;
    }

    public Integer getTotalQuota()
    {
        return totalQuota;
    }

    public void setTotalQuota(Integer totalQuota)
    {
        this.totalQuota = totalQuota;
    }

    public Integer getRegistered()
    {
        return registered;
    }

    public void setRegistered(Integer registered)
    {
        this.registered = registered;
    }

    public Integer getSignupOpen()
    {
        return signupOpen;
    }

    public void setSignupOpen(Integer signupOpen)
    {
        this.signupOpen = signupOpen;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Long getRegistrationCount()
    {
        return registrationCount;
    }

    public void setRegistrationCount(Long registrationCount)
    {
        this.registrationCount = registrationCount;
    }

    public String getCoverUrl()
    {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl)
    {
        this.coverUrl = coverUrl;
    }

    public String getIntro()
    {
        return intro;
    }

    public void setIntro(String intro)
    {
        this.intro = intro;
    }

    public BigDecimal getMileage()
    {
        return mileage;
    }

    public void setMileage(BigDecimal mileage)
    {
        this.mileage = mileage;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("location", getLocation())
            .append("startTime", getStartTime())
            .append("signupStart", getSignupStart())
            .append("signupEnd", getSignupEnd())
            .append("totalQuota", getTotalQuota())
            .append("registered", getRegistered())
            .append("signupOpen", getSignupOpen())
            .append("status", getStatus())
            .append("registrationCount", getRegistrationCount())
            .append("coverUrl", getCoverUrl())
            .append("intro", getIntro())
            .append("mileage", getMileage())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
