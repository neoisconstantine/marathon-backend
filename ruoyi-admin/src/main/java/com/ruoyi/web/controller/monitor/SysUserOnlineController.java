package com.ruoyi.web.controller.monitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
// import com.ruoyi.common.core.redis.RedisCache;       // 去Redis改造：原Redis缓存工具类，暂时停用（保留便于恢复）
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysUserOnline;
import com.ruoyi.system.service.ISysUserOnlineService;

/**
 * 在线用户监控
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/monitor/online")
public class SysUserOnlineController extends BaseController
{
    @Autowired
    private ISysUserOnlineService userOnlineService;

        // 去Redis改造：原Redis缓存工具类，暂时停用（保留便于恢复）
    // @Autowired
    // private RedisCache redisCache;

    @PreAuthorize("@ss.hasPermi('monitor:online:list')")
    @GetMapping("/list")
    public TableDataInfo list(String ipaddr, String userName)
    {
        // ============================================================================
        // 去Redis改造：在线用户列表依赖扫描Redis中所有登录token，无Redis后暂时停用
        // （返回空列表；原逻辑保留在下方注释中）
        // ============================================================================
        return getDataTable(new ArrayList<SysUserOnline>());

        // // 原逻辑（依赖Redis）
        // Collection<String> keys = redisCache.keys(CacheConstants.LOGIN_TOKEN_KEY + "*");
        // List<SysUserOnline> userOnlineList = new ArrayList<SysUserOnline>();
        // for (String key : keys)
        // {
        //     LoginUser user = redisCache.getCacheObject(key);
        //     if (StringUtils.isNotEmpty(ipaddr) && StringUtils.isNotEmpty(userName))
        //     {
        //         userOnlineList.add(userOnlineService.selectOnlineByInfo(ipaddr, userName, user));
        //     }
        //     else if (StringUtils.isNotEmpty(ipaddr))
        //     {
        //         userOnlineList.add(userOnlineService.selectOnlineByIpaddr(ipaddr, user));
        //     }
        //     else if (StringUtils.isNotEmpty(userName) && StringUtils.isNotNull(user.getUser()))
        //     {
        //         userOnlineList.add(userOnlineService.selectOnlineByUserName(userName, user));
        //     }
        //     else
        //     {
        //         userOnlineList.add(userOnlineService.loginUserToUserOnline(user));
        //     }
        // }
        // Collections.reverse(userOnlineList);
        // userOnlineList.removeAll(Collections.singleton(null));
        // return getDataTable(userOnlineList);
    }

    /**
     * 强退用户
     */
    @PreAuthorize("@ss.hasPermi('monitor:online:forceLogout')")
    @Log(title = "在线用户", businessType = BusinessType.FORCE)
    @DeleteMapping("/{tokenId}")
    public AjaxResult forceLogout(@PathVariable String tokenId)
    {
        // 去Redis改造：原逻辑删除Redis中的登录token实现强退，无Redis后暂时停用（保留便于恢复）
        // redisCache.deleteObject(CacheConstants.LOGIN_TOKEN_KEY + tokenId);
        return error("无Redis环境下在线用户强退功能暂不可用");
    }
}
