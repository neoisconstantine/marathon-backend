package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.Content;
import com.ruoyi.system.mapper.ContentMapper;
import com.ruoyi.system.service.IContentService;

/**
 * 内容管理（轮播图/公告） 服务层实现
 *
 * @author ruoyi
 */
@Service("contentService")
public class ContentServiceImpl implements IContentService
{
    @Autowired
    private ContentMapper contentMapper;

    /**
     * 查询内容列表
     *
     * @param content 内容信息
     * @return 内容集合
     */
    @Override
    public List<Content> selectContentList(Content content)
    {
        return contentMapper.selectContentList(content);
    }

    /**
     * 查询内容信息
     *
     * @param id 内容ID
     * @return 内容信息
     */
    @Override
    public Content selectContentById(Long id)
    {
        return contentMapper.selectContentById(id);
    }

    /**
     * 查询轮播图列表
     *
     * @return 轮播图集合
     */
    @Override
    public List<Content> selectBannerList()
    {
        return contentMapper.selectBannerList();
    }

    /**
     * 查询公告列表
     *
     * @return 公告集合
     */
    @Override
    public List<Content> selectNoticeList()
    {
        return contentMapper.selectNoticeList();
    }

    /**
     * 新增内容
     *
     * @param content 内容信息
     * @return 结果
     */
    @Override
    public int insertContent(Content content)
    {
        content.setCreateTime(DateUtils.getNowDate());
        content.setUpdateTime(DateUtils.getNowDate());
        if (StringUtils.isNull(content.getSort()))
        {
            content.setSort(0);
        }
        if (StringUtils.isNull(content.getStatus()))
        {
            content.setStatus(1); // 1上架
        }
        return contentMapper.insertContent(content);
    }

    /**
     * 修改内容
     *
     * @param content 内容信息
     * @return 结果
     */
    @Override
    public int updateContent(Content content)
    {
        content.setUpdateTime(DateUtils.getNowDate());
        return contentMapper.updateContent(content);
    }

    /**
     * 批量删除内容
     *
     * @param ids 需要删除的内容ID
     * @return 结果
     */
    @Override
    public int deleteContentByIds(Long[] ids)
    {
        return contentMapper.deleteContentByIds(ids);
    }
}
