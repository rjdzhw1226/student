package com.student.netty.utils;

import com.alibaba.druid.sql.visitor.functions.If;
import com.alibaba.fastjson.JSON;
import com.student.pojo.publishPo;
import com.student.pojo.vo.MessageVo;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.student.Constant.RedisKey.CHANNEL_ID_KEY;
import static com.student.Constant.RedisKey.LOCK_KEY;

@Slf4j
@Component
public class MySubscribe implements MessageListener {
    private static final Map<String,List<String>> cacheGroupMap = new ConcurrentHashMap<>();

    @Value("${netty.port}")
    private int serverPort;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        String str = new String(message.getBody());
        String key = new String(message.getChannel());
        //单聊
        if(key.contains("channel_single")){
            publishPo po = JSON.parseObject(str, publishPo.class);
            log.info("单聊：{}传输数据为：{}",po.getType(), str);
            //消息落库
            A.a.loginService.saveMessage(MessageVo.builder()
                    .messageId(po.getMessageId())
                    .message(po.getMessage())
                    .readType(po.getReadType())
                    .type(po.getType().byteValue())
                    .fromUserId(po.getFromId())
                    .toUserId(po.getId())
                    .fileType(po.getFileType())
                    .readType(po.getReadType())
                    .toGroupId(null).build()
            );
            Object channelId = A.a.redisService.nGetBinary(CHANNEL_ID_KEY, po.getId());
            Channel toUserChannel = SessionUtils.findChannelGroup((ChannelId) channelId);
            if (toUserChannel != null){
                toUserChannel.writeAndFlush(new TextWebSocketFrame(po.getJson()));
            }
        //群聊
        } else if(key.contains("channel_group")){
            List<String> userIds = null;
            publishPo po = JSON.parseObject(str, publishPo.class);
            log.info("群聊：{}传输数据为：{}",po.getType(), str);
            userIds = cacheGroupMap.get(po.getId());
            if ((userIds) == null) {
                userIds = A.a.loginService.findGroup(po.getId());
            }
            userIds.remove(po.getFromId());
            //消息落库
            A.a.loginService.saveMessage(MessageVo.builder()
                    .messageId(po.getMessageId())
                    .message(po.getMessage())
                    .type(po.getType().byteValue())
                    .fileType(po.getFileType())
                    .readType(po.getReadType())
                    .fromUserId(po.getFromId())
                    .toGroupId(po.getId()).build()
            );
            for (String userId : userIds) {
                Object channelId = A.a.redisService.nGetBinary(CHANNEL_ID_KEY, userId);
                Channel toUserChannel = SessionUtils.findChannelGroup((ChannelId) channelId);
                while (true){
                    if (!A.a.redissonClient.getLock(LOCK_KEY + userId).isLocked()) {
                        if(toUserChannel != null){
                            log.info(" toChannel 有值");
                            toUserChannel.writeAndFlush(po.getJson());
                        } else {
                            log.error("未在本机：{} toChannel 为空", serverPort);
                        }
                        break;
                    }
                }
            }
            //已读未读消息落库
            A.a.loginService.saveReadMessage(po, userIds);
        //已读未读
        } else if (key.contains("channel_read")) {
            log.info("已读未读数据为：{}", str);
            publishPo po = JSON.parseObject(str, publishPo.class);
            Object channelId = A.a.redisService.nGetBinary(CHANNEL_ID_KEY, po.getId());
            Channel toUserChannel = SessionUtils.findChannelGroup((ChannelId) channelId);
            if (toUserChannel != null){
                toUserChannel.writeAndFlush(new TextWebSocketFrame(po.getJson()));
            }
        }
        log.info("订阅频道:" + new String(message.getChannel()));
        log.info("接收数据:" + new String(message.getBody()));
    }
}

