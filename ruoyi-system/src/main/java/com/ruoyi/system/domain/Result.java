package com.ruoyi.system.domain;

import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.system.domain.vo.SegmentVo;

/**
 * 成绩对象 result
 * 
 * @author ruoyi
 */
public class Result extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 成绩ID */
    private Long id;

    /** 赛事ID */
    private Long eventId;

    /** 参赛用户ID */
    private Long personId;

    /** 报名ID */
    private Long registrationId;

    /** 参赛号码布 */
    @Excel(name = "参赛号", width = 12)
    private String bib;

    /** 枪声成绩 */
    @Excel(name = "枪声成绩", width = 15)
    private String gunTime;

    /** 净成绩 */
    @Excel(name = "净成绩", width = 15)
    private String netTime;

    /** 平均配速 */
    @Excel(name = "平均配速", width = 15)
    private String avgPace;

    /** 总排名 */
    @Excel(name = "总排名", width = 10)
    private Integer totalRank;

    /** 成绩状态（0未完赛 1已完赛 2成绩无效） */
    @Excel(name = "状态", readConverterExp = "0=未完赛,1=已完赛,2=成绩无效", width = 12)
    private Integer status;

    /** 成绩来源（0系统计算 1外部推送） */
    @Excel(name = "成绩来源", readConverterExp = "0=系统计算,1=外部推送", width = 12)
    private Integer source;

    /** 参赛用户名称 */
    @Excel(name = "姓名", width = 15)
    private String personName;

    /** 赛事名称 */
    @Excel(name = "赛事名称", width = 30)
    private String eventName;

    /** 成绩分段明细（详情查询返回） */
    private List<SegmentVo> segments;

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

    public Long getPersonId()
    {
        return personId;
    }

    public void setPersonId(Long personId)
    {
        this.personId = personId;
    }

    public Long getRegistrationId()
    {
        return registrationId;
    }

    public void setRegistrationId(Long registrationId)
    {
        this.registrationId = registrationId;
    }

    public String getBib()
    {
        return bib;
    }

    public void setBib(String bib)
    {
        this.bib = bib;
    }

    public String getGunTime()
    {
        return gunTime;
    }

    public void setGunTime(String gunTime)
    {
        this.gunTime = gunTime;
    }

    public String getNetTime()
    {
        return netTime;
    }

    public void setNetTime(String netTime)
    {
        this.netTime = netTime;
    }

    public String getAvgPace()
    {
        return avgPace;
    }

    public void setAvgPace(String avgPace)
    {
        this.avgPace = avgPace;
    }

    public Integer getTotalRank()
    {
        return totalRank;
    }

    public void setTotalRank(Integer totalRank)
    {
        this.totalRank = totalRank;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getSource()
    {
        return source;
    }

    public void setSource(Integer source)
    {
        this.source = source;
    }

    public String getPersonName()
    {
        return personName;
    }

    public void setPersonName(String personName)
    {
        this.personName = personName;
    }

    public String getEventName()
    {
        return eventName;
    }

    public void setEventName(String eventName)
    {
        this.eventName = eventName;
    }

    public List<SegmentVo> getSegments()
    {
        return segments;
    }

    public void setSegments(List<SegmentVo> segments)
    {
        this.segments = segments;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("eventId", getEventId())
            .append("personId", getPersonId())
            .append("registrationId", getRegistrationId())
            .append("bib", getBib())
            .append("gunTime", getGunTime())
            .append("netTime", getNetTime())
            .append("avgPace", getAvgPace())
            .append("totalRank", getTotalRank())
            .append("status", getStatus())
            .append("source", getSource())
            .append("personName", getPersonName())
            .append("eventName", getEventName())
            .append("segments", getSegments())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
