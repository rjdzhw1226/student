package com.student.netty.utils;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.student.Constant.RedisKey.CHANNEL_ID_KEY;

@Slf4j
@Component
public class MySubscribe implements MessageListener {

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
                    .message(po.getMessage())
                    .type(po.getType().byteValue())
                    .fromUserId(po.getFromId())
                    .toUserId(po.getId())
                    .toGroupId(null).build()
            );
            Object channelId = A.a.redisService.nGetBinary(CHANNEL_ID_KEY, po.getId());
            Channel toUserChannel = SessionUtils.findChannelGroup((ChannelId) channelId);
            toUserChannel.writeAndFlush(new TextWebSocketFrame(po.getBuf()));
        //群聊
        } else if(key.contains("channel_group")){
            publishPo po = JSON.parseObject(str, publishPo.class);
            log.info("群聊：{}传输数据为：{}",po.getType(), str);
            List<String> userIds = A.a.loginService.findGroup(po.getId());
            userIds.remove(po.getFromId());
            for (String userId : userIds) {
                Object channelId = A.a.redisService.nGetBinary(CHANNEL_ID_KEY, userId);
                Channel toUserChannel = SessionUtils.findChannelGroup((ChannelId) channelId);
                if(toUserChannel != null){
                    log.info(" toChannel 有值");
                    //消息落库
                    A.a.loginService.saveMessage(MessageVo.builder()
                            .message(po.getMessage())
                            .type(po.getType().byteValue())
                            .fromUserId(po.getFromId())
                            .toUserId(userId)
                            .toGroupId(po.getId()).build()
                    );
                    toUserChannel.writeAndFlush(po.getBuf());
                } else {
                    log.error("未在本机：{} toChannel 为空", serverPort);
                }
            }
        //已读未读
        } else if (key.contains("channel_read")) {
            log.info("已读未读数据为：{}", str);

        }
        System.out.println("订阅频道:" + new String(message.getChannel()));
        System.out.println("接收数据:" + new String(message.getBody()));
    }
}

