package com.student.netty.serve;

import com.alibaba.fastjson.JSONObject;
import com.student.netty.protocol.command.CreateGroupRequestPacket;
import com.student.netty.utils.A;
import com.student.netty.utils.SessionUtils;
import com.student.pojo.vo.User;
import com.student.util.BaseContext;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
/**
 * 创建群号ChannelHandler组件
 * @author holiday
 * 2020-11-12
 */
@Sharable
@Slf4j
public class CreateGroupRequestHandler extends SimpleChannelInboundHandler<CreateGroupRequestPacket>{

	public static CreateGroupRequestHandler INSTANCE = new CreateGroupRequestHandler();

	private CreateGroupRequestHandler () {

	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, CreateGroupRequestPacket createGroupRequestPacket) throws Exception {
		String groupId = UUID.randomUUID().toString();

		List<String> userIdList = createGroupRequestPacket.getUserIdList();
		log.info("userIdList: {}", userIdList.toString());

		List<String> nameList = A.a.loginService.queryUserByIds(userIdList);
		String userName = SessionUtils.getUser(ctx.channel()).getUserName();
		nameList.add(userName);

		userIdList.add(String.valueOf(A.a.loginService.queryUser(userName).getId()));
		//落表持久化
		A.a.loginService.saveGroupName(groupId, userIdList);
		ByteBuf byteBuf = getByteBuf(ctx, groupId, nameList);
		ctx.writeAndFlush(new TextWebSocketFrame(byteBuf));
	}

	public ByteBuf getByteBuf(ChannelHandlerContext ctx, String groupId, List<String> nameList) {
		ByteBuf bytebuf = ctx.alloc().buffer();
		JSONObject data = new JSONObject();
		data.put("type", 4);
		data.put("status", 200);
		data.put("groupId", groupId);
		data.put("nameList", nameList);
		byte []bytes = data.toJSONString().getBytes(Charset.forName("utf-8"));
		bytebuf.writeBytes(bytes);
		return bytebuf;
	}
}
