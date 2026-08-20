package com.ruoyi.framework.aspectj;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.ruoyi.common.annotation.RateLimiter;
import com.ruoyi.common.enums.LimitType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.IpUtils;

/**
 * 限流处理（内存版固定窗口计数，去Redis改造后替代原Redis+Lua实现）
 *
 * 说明：
 *  1. 单机部署场景下用 ConcurrentHashMap 固定窗口计数即可满足限流需求；
 *     若将来改为集群部署，需恢复 Redis 版实现（见文件底部历史注释的 git 记录）。
 *  2. 窗口懒清理：条目在下次访问时若已过期则重置；map 规模超过上限时全量清理过期项，
 *     防止 IP 维度的 key 被伪造请求头无限撑大。
 *
 * @author ruoyi
 */
@Aspect
@Component
public class RateLimiterAspect
{
    private static final Logger log = LoggerFactory.getLogger(RateLimiterAspect.class);

    /** 计数器 map 上限，超过后触发过期清理（防 key 无限增长） */
    private static final int MAX_COUNTER_SIZE = 50000;

    /** key → 窗口 [窗口起点ms, 已计数]（数组内变更在 compute 内进行，同一 key 互斥，线程安全） */
    private final ConcurrentHashMap<String, long[]> counters = new ConcurrentHashMap<>();

    @Before("@annotation(rateLimiter)")
    public void doBefore(JoinPoint point, RateLimiter rateLimiter) throws Throwable
    {
        int time = rateLimiter.time();
        int count = rateLimiter.count();

        String combineKey = getCombineKey(rateLimiter, point);
        long now = System.currentTimeMillis();
        long windowMillis = time * 1000L;

        long[] window = counters.compute(combineKey, (k, old) -> {
            if (old == null || now - old[0] >= windowMillis)
            {
                // 无记录或窗口已过期：开新窗口
                return new long[] { now, 1 };
            }
            old[1] = old[1] + 1;
            return old;
        });

        if (window[1] > count)
        {
            log.warn("限流拦截：key='{}', 窗口内第 {} 次请求（阈值 {} 次/{} 秒）", combineKey, window[1], count, time);
            throw new ServiceException("访问过于频繁，请稍候再试");
        }

        // map 规模保护：超过上限时清理全部过期窗口
        if (counters.size() > MAX_COUNTER_SIZE)
        {
            cleanExpired(now);
        }
    }

    /**
     * 清理已过期的计数窗口
     */
    private void cleanExpired(long now)
    {
        // 清理时需逐个判断窗口是否过期；过期判定沿用各 key 自身的最近窗口时长（保守取整体最大60s*阈值不精确，
        // 这里直接以「窗口起点距 now 超过 10 分钟」作为废弃标准，足够安全）
        Iterator<Map.Entry<String, long[]>> it = counters.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<String, long[]> entry = it.next();
            if (now - entry.getValue()[0] > 10 * 60 * 1000L)
            {
                it.remove();
            }
        }
    }

    public String getCombineKey(RateLimiter rateLimiter, JoinPoint point)
    {
        StringBuffer stringBuffer = new StringBuffer(rateLimiter.key());
        if (rateLimiter.limitType() == LimitType.IP)
        {
            stringBuffer.append(IpUtils.getIpAddr()).append("-");
        }
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Class<?> targetClass = method.getDeclaringClass();
        stringBuffer.append(targetClass.getName()).append("-").append(method.getName());
        return stringBuffer.toString();
    }
}
