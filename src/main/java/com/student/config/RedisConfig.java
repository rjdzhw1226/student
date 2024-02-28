package com.student.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.student.netty.utils.MySubscribe;
import com.student.util.redis.FastJsonJsonRedisSerializer;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
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
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = Jackson2JsonRedisSerializer();

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        redisTemplate.setKeySerializer(StringRedisSerializer.UTF_8);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);

        redisTemplate.setHashKeySerializer(StringRedisSerializer.UTF_8);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    public Jackson2JsonRedisSerializer<Object> Jackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        serializer.setObjectMapper(objectMapper);
        return serializer;
    }

    @Bean
    @ConditionalOnMissingBean(name = "redisObjectTemplate")
    public RedisTemplate<Object, Object> redisObjectTemplate(RedisConnectionFactory redisConnectionFactory) {
        JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
        RedisTemplate<Object, Object> redisObjectTemplate = new RedisTemplate<>();
        redisObjectTemplate.setConnectionFactory(redisConnectionFactory);

        redisObjectTemplate.setKeySerializer(StringRedisSerializer.UTF_8);
        redisObjectTemplate.setValueSerializer(jdkSerializationRedisSerializer);
        redisObjectTemplate.setHashKeySerializer(StringRedisSerializer.UTF_8);
        redisObjectTemplate.setHashValueSerializer(jdkSerializationRedisSerializer);
        redisObjectTemplate.afterPropertiesSet();
        return redisObjectTemplate;
    }

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        //订阅频道 通配符*
        container.addMessageListener(new MySubscribe(), new PatternTopic("channel*"));
        return container;
    }

    @Bean
    public RedissonClient redissonClient(){

        //配置
        //ningzaichun
        Config config = new Config();
        //config.useSingleServer().setAddress("redis://192.168.179.200:6379").setPassword("ningzaichun");
        //config.useSingleServer().setAddress("redis://192.168.88.130:6379").setPassword("123321");
        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setPassword("123321");
        //config.useSingleServer().setAddress("redis://192.168.0.108:6379").setPassword("123321");

        //返回配置类redisson，创建客户端
        return Redisson.create(config);


    }


}
