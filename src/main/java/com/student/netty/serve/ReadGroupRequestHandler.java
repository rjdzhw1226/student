package com.student.netty.serve;

import com.alibaba.fastjson.JSONObject;
import com.student.netty.protocol.command.ReadGroupRequestPacket;
import com.student.netty.protocol.command.ReadRequestPacket;
import com.student.netty.utils.A;
import com.student.netty.utils.SessionUtils;
import com.student.pojo.ReadResult;
import com.student.pojo.publishPo;
import com.student.pojo.vo.User;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Sharable
public class ReadGroupRequestHandler extends SimpleChannelInboundHandler<ReadGroupRequestPacket> {

    public static ReadGroupRequestHandler INSTANCE = new ReadGroupRequestHandler();

    private ReadGroupRequestHandler () {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ReadGroupRequestPacket readGroupRequestPacket) throws Exception {
        //根据messageId 查对应数据的已读未读次数 发送已读回执给发送人
        Channel toUserChannel = SessionUtils.getChannel(readGroupRequestPacket.getToUserId());
        if (toUserChannel != null && SessionUtils.hasLogin(toUserChannel)) {

        } else {
            //分布式发送
            //查 多少人读过 群人数
            ReadResult readResult = A.a.loginService.selectGroupMessage(readGroupRequestPacket);
            String json = getByteBufRes(readGroupRequestPacket, readResult);
            publishPo po = new publishPo(22,readGroupRequestPacket.getToUserId(),readGroupRequestPacket.getFromUserId(),"已读未读回执",null, json, readGroupRequestPacket.getMessageId(), null,null, readGroupRequestPacket.getGroupId());
            A.a.redisService.publish("channel_read", po);
        }
        //异步更新 index表
        A.a.loginService.asyncUpdateGroupMessage(readGroupRequestPacket);
    }

    public String getByteBufRes(ReadGroupRequestPacket readGroupRequestPacket, ReadResult readResult) {
        JSONObject data = new JSONObject();
        data.put("type", 22);
        data.put("status", 200);
        JSONObject params = new JSONObject();
        params.put("messageId", readGroupRequestPacket.getMessageId());
        params.put("readCount", readResult.getReadCount());
        params.put("unReadCount", readResult.getUnReadCount());
        params.put("groupCount", readResult.getGroupCount());
        params.put("groupId", readGroupRequestPacket.getGroupId());
        params.put("fromUser", readGroupRequestPacket.getFromUserId());
        params.put("toUser",readGroupRequestPacket.getToUserId());
        data.put("params", params);
        return data.toJSONString();
    }
}
