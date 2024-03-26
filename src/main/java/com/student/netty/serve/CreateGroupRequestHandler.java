package com.student.netty.serve;

import com.alibaba.fastjson.JSONObject;
import com.student.netty.protocol.command.CreateGroupRequestPacket;
import com.student.netty.utils.A;
import com.student.netty.utils.SessionUtils;
import com.student.pojo.publishPo;
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
import java.util.Map;
import java.util.UUID;
/**
 * 创建群号ChannelHandler组件
 *
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
		String groupId = UUID.randomUUID().toString().replace("-","");
		String userName = SessionUtils.getUser(ctx.channel()).getUserName();

		List<String> userIdList = new ArrayList<>(createGroupRequestPacket.getUserIdList());
		log.info("userIdList: {}", userIdList);

		Map<String, List<String>> nameList = A.a.loginService.addChat(userIdList, userName, groupId, createGroupRequestPacket.getChatType(), createGroupRequestPacket.getTitle());
		String json = getByteBuf(groupId, nameList.get("name"), createGroupRequestPacket.getTitle());
		publishPo po = new publishPo(json, nameList.get("id"));
		//广播通知
		A.a.redisService.publish("channel_create", po);
		//ctx.writeAndFlush(new TextWebSocketFrame(byteBuf));
	}

	public String getByteBuf(String groupId, List<String> nameList, String title) {
		JSONObject data = new JSONObject();
		data.put("type", 4);
		data.put("status", 200);
		data.put("groupId", groupId);
		data.put("nameList", nameList);
		data.put("groupCount", nameList.size());
		data.put("image", title + ".png");
		return data.toJSONString();
	}
}
