package com.ruoyi.framework.aspectj;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.IpUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 防重复提交切面（内存版，去Redis改造后以AOP方式替代原SameUrlDataInterceptor拦截器方案）
 *
 * 判定规则：同一提交主体（token，无token时用IP）+ 同一URL + 相同参数，
 * 在 interval 毫秒内重复提交则拒绝。适用于POST表单/下单等写操作接口。
 *
 * @author ruoyi
 */
@Aspect
@Component
public class RepeatSubmitAspect
{
    private static final Logger log = LoggerFactory.getLogger(RepeatSubmitAspect.class);

    /** 上次提交时间 map 上限，超过后触发过期清理（防 key 无限增长） */
    private static final int MAX_RECORD_SIZE = 50000;

    /** key → 上次提交时间ms */
    private final ConcurrentHashMap<String, Long> lastSubmitMap = new ConcurrentHashMap<>();

    @Before("@annotation(repeatSubmit)")
    public void doBefore(JoinPoint point, RepeatSubmit repeatSubmit) throws Throwable
    {
        long now = System.currentTimeMillis();
        long interval = repeatSubmit.interval();

        String submitKey = buildSubmitKey(point);
        Long lastTime = lastSubmitMap.put(submitKey, now);
        if (lastTime != null && now - lastTime < interval)
        {
            log.warn("重复提交拦截：key='{}', 距上次提交 {}ms（阈值 {}ms）", submitKey, now - lastTime, interval);
            throw new ServiceException(repeatSubmit.message());
        }

        // map 规模保护：超过上限时清理过期记录
        if (lastSubmitMap.size() > MAX_RECORD_SIZE)
        {
            cleanExpired(now, interval);
        }
    }

    /**
     * 构建提交唯一键：URL + 提交主体（Authorization/wx-token，无则IP）+ 参数摘要
     */
    private String buildSubmitKey(JoinPoint point)
    {
        HttpServletRequest request = currentRequest();
        String url = request != null ? request.getRequestURI() : "unknown-url";

        // 提交主体：优先取登录令牌（管理端 Authorization / 小程序 wx-token），无令牌时退化为客户端IP
        String subject = null;
        if (request != null)
        {
            subject = request.getHeader("Authorization");
            if (StringUtils.isBlank(subject))
            {
                subject = request.getHeader("wx-token");
            }
        }
        if (StringUtils.isBlank(subject))
        {
            subject = IpUtils.getIpAddr();
        }

        return url + "|" + subject + "|" + argsDigest(point);
    }

    /**
     * 参数摘要：排除 Servlet 请求/响应与文件对象后 JSON 序列化，序列化失败时退化为 toString
     */
    private String argsDigest(JoinPoint point)
    {
        StringBuilder sb = new StringBuilder();
        Object[] args = point.getArgs();
        if (args != null)
        {
            for (Object arg : args)
            {
                if (arg == null || arg instanceof HttpServletRequest || arg instanceof HttpServletResponse
                        || arg instanceof MultipartFile || arg instanceof byte[])
                {
                    continue;
                }
                sb.append(safeStringify(arg)).append(";");
            }
        }
        return sb.length() > 0 ? sb.toString() : "no-args";
    }

    private String safeStringify(Object arg)
    {
        try
        {
            String json = JSON.toJSONString(arg);
            // 参数过长时截断（防大 body 撑爆内存）
            return json.length() > 500 ? json.substring(0, 500) : json;
        }
        catch (Exception e)
        {
            String str = String.valueOf(arg);
            return str.length() > 500 ? str.substring(0, 500) : str;
        }
    }

    private HttpServletRequest currentRequest()
    {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs != null ? attrs.getRequest() : null;
    }

    /**
     * 清理超过阈值间隔的历史记录（以提交间隔上限为废弃标准，实际过期间隔取当前注解值）
     */
    private void cleanExpired(long now, long interval)
    {
        Iterator<Map.Entry<String, Long>> it = lastSubmitMap.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<String, Long> entry = it.next();
            if (now - entry.getValue() > Math.max(interval, 60 * 1000L))
            {
                it.remove();
            }
        }
    }
}
