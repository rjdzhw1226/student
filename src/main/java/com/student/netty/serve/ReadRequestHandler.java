package com.student.netty.serve;

import com.alibaba.fastjson.JSONObject;
import com.student.netty.protocol.command.MessageRequestPacket;
import com.student.netty.protocol.command.ReadRequestPacket;
import com.student.netty.utils.A;
import com.student.netty.utils.SessionUtils;
import com.student.pojo.publishPo;
import com.student.pojo.vo.User;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import static com.student.Constant.RedisKey.ONLINE_SIGN;

@Sharable
@Slf4j
public class ReadRequestHandler extends SimpleChannelInboundHandler<ReadRequestPacket> {

    public static ReadRequestHandler INSTANCE = new ReadRequestHandler();

    private ReadRequestHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ReadRequestPacket readRequestPacket) throws Exception {
        Channel toUserChannel = SessionUtils.getChannel(readRequestPacket.getToUserId());
        if (toUserChannel != null && SessionUtils.hasLogin(toUserChannel)) {
            //本机发送

        } else {
            log.error("当前用户：" + readRequestPacket.getToUserId() + "未在本机登录！其它服务器在线标识：{} (1 已读 0 未读)", readRequestPacket.getReadType());
            String json = getByteBufRes(readRequestPacket);
            String toUserId = readRequestPacket.getToUserId();
            String fromId = readRequestPacket.getFromUserid();
            publishPo po = new publishPo(20,toUserId,fromId,"已读未读回执",null, json, readRequestPacket.getMessageId(), readRequestPacket.getReadType(), readRequestPacket.getFileType());
            //分布式发送
            A.a.redisService.publish("channel_read", po);
        }
        //异步更新数据库
        A.a.loginService.asyncUpdateMessage(readRequestPacket);
    }


    public String getByteBufRes(ReadRequestPacket readRequestPacket) {
        JSONObject data = new JSONObject();
        data.put("type", 20);
        data.put("status", 200);
        JSONObject params = new JSONObject();
        params.put("messageId", readRequestPacket.getMessageId());
        params.put("readType", readRequestPacket.getReadType());
        params.put("fileType", readRequestPacket.getFileType());
        params.put("fromUser", readRequestPacket.getFromUserid());
        params.put("toUser",readRequestPacket.getToUserId());
        data.put("params", params);
        return data.toJSONString();
    }

}
