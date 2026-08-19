package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Content;

/**
 * 内容管理（轮播图/公告） 服务层
 *
 * @author ruoyi
 */
public interface IContentService
{
    /**
     * 查询内容列表
     *
     * @param content 内容信息
     * @return 内容集合
     */
    public List<Content> selectContentList(Content content);

    /**
     * 查询内容信息
     *
     * @param id 内容ID
     * @return 内容信息
     */
    public Content selectContentById(Long id);

    /**
     * 查询轮播图列表
     *
     * @return 轮播图集合
     */
    public List<Content> selectBannerList();

    /**
     * 查询公告列表
     *
     * @return 公告集合
     */
    public List<Content> selectNoticeList();

    /**
     * 新增内容
     *
     * @param content 内容信息
     * @return 结果
     */
    public int insertContent(Content content);

    /**
     * 修改内容
     *
     * @param content 内容信息
     * @return 结果
     */
    public int updateContent(Content content);

    /**
     * 批量删除内容
     *
     * @param ids 需要删除的内容ID
     * @return 结果
     */
    public int deleteContentByIds(Long[] ids);
}
