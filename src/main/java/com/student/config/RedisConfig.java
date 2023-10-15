package com.student.config;

import com.student.util.redis.FastJsonJsonRedisSerializer;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig extends CachingConfigurerSupport {

//    @Bean
//    @SuppressWarnings(value = {"unchecked","rawtypes"})
//    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
//        RedisTemplate<Object, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(connectionFactory);
//        FastJsonJsonRedisSerializer serializer =new FastJsonJsonRedisSerializer(Object.class);
//
//        //使用stringRedisSerializer来序列化redis的key值
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(serializer);
//
//        //Hash的key
//        template.setHashValueSerializer(new StringRedisSerializer());
//        template.setHashValueSerializer(serializer);
//
//        template.afterPropertiesSet();
//        return template;
//    }

    @Bean
    public RedissonClient redissonClient(){

        //配置
        //ningzaichun
        Config config = new Config();
        //config.useSingleServer().setAddress("redis://192.168.179.200:6379");
        config.useSingleServer().setAddress("redis://192.168.88.130:6379").setPassword("123321");
        //config.useSingleServer().setAddress("redis://172.18.0.3:6379").setPassword("123321");
//        config.useSingleServer().setAddress("redis://172.19.0.4:6379").setPassword("ningzaichun");
        //config.useSingleServer().setAddress("redis://127.0.0.1:6379");

        //返回配置类redisson，创建客户端
        return Redisson.create(config);


    }


}
