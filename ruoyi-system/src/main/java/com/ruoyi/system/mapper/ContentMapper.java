package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.Content;

/**
 * 资讯 数据层
 * 
 * @author ruoyi
 */
public interface ContentMapper
{
    /**
     * 通过ID查询资讯
     * 
     * @param id 资讯ID
     * @return 资讯信息
     */
    public Content selectContentById(Long id);

    /**
     * 查询资讯列表
     * 
     * @param content 资讯信息
     * @return 资讯集合
     */
    public List<Content> selectContentList(Content content);

    /**
     * 查询上架中的轮播图列表
     * 
     * @return 轮播图集合
     */
    public List<Content> selectBannerList();

    /**
     * 查询上架中的公告列表
     * 
     * @return 公告集合
     */
    public List<Content> selectNoticeList();

    /**
     * 新增资讯
     * 
     * @param content 资讯信息
     * @return 结果
     */
    public int insertContent(Content content);

    /**
     * 修改资讯
     * 
     * @param content 资讯信息
     * @return 结果
     */
    public int updateContent(Content content);

    /**
     * 批量删除资讯
     * 
     * @param ids 需要删除的资讯ID
     * @return 结果
     */
    public int deleteContentByIds(Long[] ids);
}
