package com.student.netty.serve;

import com.alibaba.fastjson.JSONObject;
import com.student.netty.protocol.command.GroupMessageRequestPacket;
import com.student.netty.utils.A;
import com.student.netty.utils.SessionUtils;
import com.student.pojo.publishPo;
import com.student.pojo.vo.User;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * 发送群消息handler组件
 */
@Sharable
public class GroupMessageRequestHandler extends SimpleChannelInboundHandler<GroupMessageRequestPacket>{

	public static GroupMessageRequestHandler INSTANCE = new GroupMessageRequestHandler();

	private GroupMessageRequestHandler () {

	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, GroupMessageRequestPacket groupMessageRequestPacket) throws Exception {
		//分布式发送
		String groupId = groupMessageRequestPacket.getToGroupId();
		String json = getByteBufRes(ctx, groupMessageRequestPacket);
		publishPo po = new publishPo(9, groupId, groupMessageRequestPacket.getFromUserId(),groupMessageRequestPacket.getMessage(),null, json, null);
		A.a.redisService.publish("channel_group", po);
	}

	public ByteBuf getByteBuf(ChannelHandlerContext ctx, String groupId, String message,
			                  User fromUser, String fileType, List<String> nameList) {
		ByteBuf byteBuf = ctx.alloc().buffer();
		JSONObject data = new JSONObject();
		data.put("type", 10);
		data.put("status", 200);
		JSONObject params = new JSONObject();
		params.put("message", message);
		params.put("fileType", fileType);
		params.put("fromUser", fromUser);
		params.put("groupId", groupId);
		Collections.reverse(nameList);
		params.put("nameList", nameList);
		data.put("params", params);
		byte []bytes = data.toJSONString().getBytes(Charset.forName("utf-8"));
		byteBuf.writeBytes(bytes);
		return byteBuf;
	}

	public String getByteBufRes(ChannelHandlerContext ctx, GroupMessageRequestPacket groupMessageRequestPacket) {
		User fromUser = A.a.loginService.queryUserById(groupMessageRequestPacket.getFromUserId());
		List<String> nameList = A.a.loginService.findGroup(groupMessageRequestPacket.getToGroupId());
		JSONObject data = new JSONObject();
		data.put("type", 10);
		data.put("status", 200);
		JSONObject params = new JSONObject();
		params.put("message", groupMessageRequestPacket.getMessage());
		params.put("fileType", groupMessageRequestPacket.getFileType());
		params.put("fromUser", fromUser);
		params.put("groupId", groupMessageRequestPacket.getToGroupId());
		params.put("nameList", nameList);
		data.put("params", params);
		return data.toJSONString();
	}
}
