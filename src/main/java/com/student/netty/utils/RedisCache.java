package com.student.netty.utils;

import com.student.pojo.publishPo;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Configuration
@EnableCaching
@Slf4j
public class RedisCache {
    @Resource(name = "redisObjectTemplate")
    private RedisTemplate<Object, Object> redisObjectTemplate;

    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 缓存netty
     *
     * @param key
     * @param item
     * @param value
     * @return
     */
    public Boolean nSetBinary(String key, String item, Object value) {
        try {
            redisObjectTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            log.error("Exception:{0}", e);
            return false;
        }
    }
    public Object nGetBinary(String key, String item) {
        Object o = null;
        try{
            o = redisObjectTemplate.opsForHash().get(key, item);
        }catch (Exception e){
            e.printStackTrace();
        }
        return o;
    }
    public void nDelBinary(String key, Object... item) {
        redisObjectTemplate.opsForHash().delete(key, item);
    }

    public boolean set(String key,Object value,long time){
        try {
            if(time>0){
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            }else{
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean set(String key,Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public Object get(String key){
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    public void del(String key){
        if(key != null && !"".equals(key)){
            redisTemplate.delete(key);
        }
    }

    public void publish(String publishKey, publishPo msg) {
        redisTemplate.convertAndSend(publishKey, msg);
    }
}
