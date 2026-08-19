package com.ruoyi.framework.interceptor;

import java.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.domain.ApiResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.WxSecurityUtils;
import com.ruoyi.framework.web.service.WxTokenService;

/**
 * 小程序登录鉴权拦截器（/api/**路径由本拦截器独立鉴权，不走Spring Security）
 *
 * @author ruoyi
 */
@Component
public class WxAuthInterceptor implements HandlerInterceptor
{
    /** 请求属性：当前登录人员ID */
    public static final String ATTR_PERSON_ID = "wx_person_id";

    @Autowired
    private WxTokenService wxTokenService;

    /**
     * 校验wx-token，通过后将人员ID写入请求属性与ThreadLocal
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        String token = wxTokenService.getToken(request);
        if (StringUtils.isBlank(token))
        {
            writeUnauthorized(response);
            return false;
        }
        try
        {
            Long personId = wxTokenService.getPersonId(token);
            if (StringUtils.isNull(personId))
            {
                writeUnauthorized(response);
                return false;
            }
            request.setAttribute(ATTR_PERSON_ID, personId);
            WxSecurityUtils.setPersonId(personId);
            return true;
        }
        catch (Exception e)
        {
            writeUnauthorized(response);
            return false;
        }
    }

    /**
     * 请求结束后清理ThreadLocal，防止线程复用导致的数据串用
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
    {
        WxSecurityUtils.clear();
    }

    /**
     * 输出未登录应答（HTTP 401 + ApiResult JSON）
     */
    private void writeUnauthorized(HttpServletResponse response) throws IOException
    {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(ApiResult.error(401, "未登录或登录已过期")));
    }
}
