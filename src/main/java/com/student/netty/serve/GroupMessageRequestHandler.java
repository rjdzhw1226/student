package com.student.netty.serve;

import com.alibaba.fastjson.JSONObject;
import com.student.netty.protocol.command.GroupMessageRequestPacket;
import com.student.netty.utils.SessionUtils;
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
 * @author holiday
 * 2020-11-16
 */
@Sharable
public class GroupMessageRequestHandler extends SimpleChannelInboundHandler<GroupMessageRequestPacket>{

	public static GroupMessageRequestHandler INSTANCE = new GroupMessageRequestHandler();

	private GroupMessageRequestHandler () {

	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, GroupMessageRequestPacket groupMessageRequestPacket) throws Exception {

		String groupId = groupMessageRequestPacket.getToGroupId();
		String fileType = groupMessageRequestPacket.getFileType();
		ChannelGroup channelGroup = SessionUtils.getChannelGroup(groupId);
		List<String> nameList = new ArrayList<>();
		for (Channel channel : channelGroup) {
			User user = SessionUtils.getUser(channel);
			nameList.add(user.getShowName());
		}
		if (channelGroup != null) {
			User user = SessionUtils.getUser(ctx.channel());
			ByteBuf byteBuf = getByteBuf(ctx, groupId, groupMessageRequestPacket.getMessage(), user, fileType, nameList);
			channelGroup.remove(ctx.channel());//发送方不需要自己再收到消息
			channelGroup.writeAndFlush(new TextWebSocketFrame(byteBuf));
			channelGroup.add(ctx.channel()); //发送完消息再添加回去 ---todo 是否有更好得方式
		}
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
}
