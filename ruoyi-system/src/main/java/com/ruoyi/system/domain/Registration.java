package com.ruoyi.system.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 报名对象 registration
 * 
 * @author ruoyi
 */
public class Registration extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 报名ID */
    private Long id;

    /** 参赛用户ID */
    private Long personId;

    /** 赛事ID */
    private Long eventId;

    /** 参赛号码布 */
    private String bib;

    /** 报名状态（0已报名 1已审核 2已退赛） */
    private Integer status;

    /** 参赛用户名称 */
    private String personName;

    /** 参赛用户手机号码 */
    private String personPhone;

    /** 赛事名称 */
    private String eventName;

    /** 赛事开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date eventStartTime;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getPersonId()
    {
        return personId;
    }

    public void setPersonId(Long personId)
    {
        this.personId = personId;
    }

    public Long getEventId()
    {
        return eventId;
    }

    public void setEventId(Long eventId)
    {
        this.eventId = eventId;
    }

    public String getBib()
    {
        return bib;
    }

    public void setBib(String bib)
    {
        this.bib = bib;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public String getPersonName()
    {
        return personName;
    }

    public void setPersonName(String personName)
    {
        this.personName = personName;
    }

    public String getPersonPhone()
    {
        return personPhone;
    }

    public void setPersonPhone(String personPhone)
    {
        this.personPhone = personPhone;
    }

    public String getEventName()
    {
        return eventName;
    }

    public void setEventName(String eventName)
    {
        this.eventName = eventName;
    }

    public Date getEventStartTime()
    {
        return eventStartTime;
    }

    public void setEventStartTime(Date eventStartTime)
    {
        this.eventStartTime = eventStartTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("personId", getPersonId())
            .append("eventId", getEventId())
            .append("bib", getBib())
            .append("status", getStatus())
            .append("personName", getPersonName())
            .append("personPhone", getPersonPhone())
            .append("eventName", getEventName())
            .append("eventStartTime", getEventStartTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
