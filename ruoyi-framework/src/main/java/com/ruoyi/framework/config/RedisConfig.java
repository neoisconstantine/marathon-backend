// 去Redis改造：本文件整体暂时停用（逐行注释，保留代码便于恢复）
//  * ============================================================================
//  * 去Redis改造：RedisConfig 配置类整体暂时停用（保留代码便于恢复）
//  * 若需恢复Redis缓存功能，取消本文件注释并恢复 ruoyi-common/pom.xml 中的
//  * spring-boot-starter-data-redis 依赖即可。
//  * ============================================================================
// package com.ruoyi.framework.config;
// 
// import org.springframework.cache.annotation.CachingConfigurerSupport;
// import org.springframework.cache.annotation.EnableCaching;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.data.redis.connection.RedisConnectionFactory;
// import org.springframework.data.redis.core.RedisTemplate;
// import org.springframework.data.redis.core.script.DefaultRedisScript;
// import org.springframework.data.redis.serializer.StringRedisSerializer;
// 
// /**
//  * redis配置
//  * 
//  * @author ruoyi
//  */
// @SuppressWarnings("deprecation")
// @Configuration
// @EnableCaching
// public class RedisConfig extends CachingConfigurerSupport
// {
//     @Bean
//     @SuppressWarnings(value = { "unchecked", "rawtypes" })
//     public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory)
//     {
//         RedisTemplate<Object, Object> template = new RedisTemplate<>();
//         template.setConnectionFactory(connectionFactory);
// 
//         FastJson2JsonRedisSerializer serializer = new FastJson2JsonRedisSerializer(Object.class);
// 
//         // 使用StringRedisSerializer来序列化和反序列化redis的key值
//         template.setKeySerializer(new StringRedisSerializer());
//         template.setValueSerializer(serializer);
// 
//         // Hash的key也采用StringRedisSerializer的序列化方式
//         template.setHashKeySerializer(new StringRedisSerializer());
//         template.setHashValueSerializer(serializer);
// 
//         template.afterPropertiesSet();
//         return template;
//     }
// 
//     @Bean
//     public DefaultRedisScript<Long> limitScript()
//     {
//         DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
//         redisScript.setScriptText(limitScriptText());
//         redisScript.setResultType(Long.class);
//         return redisScript;
//     }
// 
//     /**
//      * 限流脚本
//      */
//     private String limitScriptText()
//     {
//         return "local key = KEYS[1]\n" +
//                 "local count = tonumber(ARGV[1])\n" +
//                 "local time = tonumber(ARGV[2])\n" +
//                 "local current = redis.call('get', key);\n" +
//                 "if current and tonumber(current) > count then\n" +
//                 "    return tonumber(current);\n" +
//                 "end\n" +
//                 "current = redis.call('incr', key)\n" +
//                 "if tonumber(current) == 1 then\n" +
//                 "    redis.call('expire', key, time)\n" +
//                 "end\n" +
//                 "return tonumber(current);";
//     }
// }
// */
