package com.ruoyi.framework.config;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.framework.interceptor.WxAuthInterceptor;
// 去Redis改造：防重复提交拦截器依赖Redis，暂时停用（保留便于恢复）
// import com.ruoyi.framework.interceptor.RepeatSubmitInterceptor;

/**
 * 通用配置
 * 
 * @author ruoyi
 */
@Configuration
public class ResourcesConfig implements WebMvcConfigurer
{
    // 去Redis改造：防重复提交拦截器依赖Redis，暂时停用（保留便于恢复）
    // @Autowired
    // private RepeatSubmitInterceptor repeatSubmitInterceptor;

    /** 小程序登录鉴权拦截器 */
    @Autowired
    private WxAuthInterceptor wxAuthInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        /** 本地文件上传路径 */
        registry.addResourceHandler(Constants.RESOURCE_PREFIX + "/**")
                .addResourceLocations("file:" + RuoYiConfig.getProfile() + "/");

        /** swagger配置 */
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .setCacheControl(CacheControl.maxAge(5, TimeUnit.HOURS).cachePublic());
    }

    /**
     * 自定义拦截规则
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        // 去Redis改造：防重复提交拦截器依赖Redis，暂时停用（保留便于恢复）
        // registry.addInterceptor(repeatSubmitInterceptor).addPathPatterns("/**");
        // 小程序接口鉴权拦截器（公开接口放行：登录/监控/赛事/资讯/赛道轨迹；报名、报名记录、成绩、个人信息需登录）
        registry.addInterceptor(wxAuthInterceptor).addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/auth/wx-login",
                        "/api/monitor/**",
                        "/api/event/**",
                        "/api/content/**",
                        "/api/camera/**",
                        "/api/result/event");
    }

    /**
     * 跨域配置
     */
    @Bean
    public CorsFilter corsFilter()
    {
        CorsConfiguration config = new CorsConfiguration();
        // 设置访问源地址
        config.addAllowedOriginPattern("*");
        // 设置访问源请求头
        config.addAllowedHeader("*");
        // 设置访问源请求方法
        config.addAllowedMethod("*");
        // 有效期 1800秒
        config.setMaxAge(1800L);
        // 添加映射路径，拦截一切请求
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        // 返回新的CorsFilter
        return new CorsFilter(source);
    }
}