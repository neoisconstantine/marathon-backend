package com.ruoyi.system.service.impl;

import java.util.Collection;
import java.util.List;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
// import com.ruoyi.common.constant.CacheConstants;        // 去Redis改造：原Redis缓存key常量，暂时停用（保留便于恢复）
import com.ruoyi.common.constant.UserConstants;
// import com.ruoyi.common.core.redis.RedisCache;          // 去Redis改造：原Redis缓存工具类，暂时停用（保留便于恢复）
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.mapper.SysConfigMapper;
import com.ruoyi.system.service.ISysConfigService;

/**
 * 参数配置 服务层实现
 * 
 * @author ruoyi
 */
@Service
public class SysConfigServiceImpl implements ISysConfigService
{
    @Autowired
    private SysConfigMapper configMapper;

    // 去Redis改造：原Redis缓存工具类，暂时停用（保留便于恢复）
    // @Autowired
    // private RedisCache redisCache;

    /**
     * 项目启动时，初始化参数到缓存
     */
    // 去Redis改造：原逻辑启动时全量加载参数配置到Redis缓存，暂时停用（保留便于恢复）
    // @PostConstruct
    // public void init()
    // {
    //     loadingConfigCache();
    // }

    /**
     * 查询参数配置信息
     * 
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    @Override
    public SysConfig selectConfigById(Long configId)
    {
        SysConfig config = new SysConfig();
        config.setConfigId(configId);
        return configMapper.selectConfig(config);
    }

    /**
     * 根据键名查询参数配置信息
     * 
     * @param configKey 参数key
     * @return 参数键值
     */
    @Override
    public String selectConfigByKey(String configKey)
    {
        // 去Redis改造：原逻辑先读Redis缓存，未命中再查库并回填缓存；现直接查询数据库
        // String configValue = Convert.toStr(redisCache.getCacheObject(getCacheKey(configKey)));
        // if (StringUtils.isNotEmpty(configValue))
        // {
        //     return configValue;
        // }
        SysConfig config = new SysConfig();
        config.setConfigKey(configKey);
        SysConfig retConfig = configMapper.selectConfig(config);
        if (StringUtils.isNotNull(retConfig))
        {
            // redisCache.setCacheObject(getCacheKey(configKey), retConfig.getConfigValue());
            return retConfig.getConfigValue();
        }
        return StringUtils.EMPTY;
    }

    /**
     * 获取验证码开关
     * 
     * @return true开启，false关闭
     */
    @Override
    public boolean selectCaptchaEnabled()
    {
        String captchaEnabled = selectConfigByKey("sys.account.captchaEnabled");
        if (StringUtils.isEmpty(captchaEnabled))
        {
            return true;
        }
        return Convert.toBool(captchaEnabled);
    }

    /**
     * 查询参数配置列表
     * 
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    @Override
    public List<SysConfig> selectConfigList(SysConfig config)
    {
        return configMapper.selectConfigList(config);
    }

    /**
     * 新增参数配置
     * 
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int insertConfig(SysConfig config)
    {
        int row = configMapper.insertConfig(config);
        // 去Redis改造：原逻辑写入Redis缓存，暂时停用
        // if (row > 0)
        // {
        //     redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        // }
        return row;
    }

    /**
     * 修改参数配置
     * 
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int updateConfig(SysConfig config)
    {
        // SysConfig temp = configMapper.selectConfigById(config.getConfigId());
        // if (!StringUtils.equals(temp.getConfigKey(), config.getConfigKey()))
        // {
        //     redisCache.deleteObject(getCacheKey(temp.getConfigKey()));
        // }

        int row = configMapper.updateConfig(config);
        // 去Redis改造：原逻辑更新Redis缓存，暂时停用
        // if (row > 0)
        // {
        //     redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        // }
        return row;
    }

    /**
     * 批量删除参数信息
     * 
     * @param configIds 需要删除的参数ID
     */
    @Override
    public void deleteConfigByIds(Long[] configIds)
    {
        for (Long configId : configIds)
        {
            SysConfig config = selectConfigById(configId);
            if (StringUtils.equals(UserConstants.YES, config.getConfigType()))
            {
                throw new ServiceException(String.format("内置参数【%1$s】不能删除 ", config.getConfigKey()));
            }
            configMapper.deleteConfigById(configId);
            // 去Redis改造：原逻辑删除Redis缓存，暂时停用
            // redisCache.deleteObject(getCacheKey(config.getConfigKey()));
        }
    }

    /**
     * 加载参数缓存数据
     */
    @Override
    public void loadingConfigCache()
    {
        // 去Redis改造：原逻辑全量加载参数配置到Redis缓存，暂时停用（保留便于恢复）
        // List<SysConfig> configsList = configMapper.selectConfigList(new SysConfig());
        // for (SysConfig config : configsList)
        // {
        //     redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        // }
    }

    /**
     * 清空参数缓存数据
     */
    @Override
    public void clearConfigCache()
    {
        // 去Redis改造：原逻辑清空Redis缓存，暂时停用（保留便于恢复）
        // Collection<String> keys = redisCache.keys(CacheConstants.SYS_CONFIG_KEY + "*");
        // redisCache.deleteObject(keys);
    }

    /**
     * 重置参数缓存数据
     */
    @Override
    public void resetConfigCache()
    {
        // 去Redis改造：无缓存可重置，暂时停用（保留便于恢复）
        // clearConfigCache();
        // loadingConfigCache();
    }

    /**
     * 校验参数键名是否唯一
     * 
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public boolean checkConfigKeyUnique(SysConfig config)
    {
        Long configId = StringUtils.isNull(config.getConfigId()) ? -1L : config.getConfigId();
        SysConfig info = configMapper.checkConfigKeyUnique(config.getConfigKey());
        if (StringUtils.isNotNull(info) && info.getConfigId().longValue() != configId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 设置cache key
     * 
     * @param configKey 参数键
     * @return 缓存键key
     */
    // 去Redis改造：无Redis后不再需要缓存key拼接，原方法暂时停用（保留便于恢复）
    // private String getCacheKey(String configKey)
    // {
    //     return CacheConstants.SYS_CONFIG_KEY + configKey;
    // }
}
