package com.student.netty.serve;

import com.alibaba.fastjson.JSONObject;
import com.student.netty.protocol.command.GroupMessageRequestPacket;
import com.student.netty.utils.A;
import com.student.pojo.publishPo;
import com.student.pojo.vo.User;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

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
		//分布式发送 还待完善 没写完 群聊在多节点发送时加锁
		String groupId = groupMessageRequestPacket.getToGroupId();
		String json = getByteBufRes(groupMessageRequestPacket);
		publishPo po = new publishPo(9, groupId, groupMessageRequestPacket.getFromUserId(),groupMessageRequestPacket.getMessage(),null, json, groupMessageRequestPacket.getMessageId(),"0", "0");
		A.a.redisService.publish("channel_group", po);
	}

	public String getByteBufRes(GroupMessageRequestPacket groupMessageRequestPacket) {
		User fromUser = A.a.loginService.queryUserById(groupMessageRequestPacket.getFromUserId());
		List<String> nameList = A.a.loginService.findGroup(groupMessageRequestPacket.getToGroupId());
		JSONObject data = new JSONObject();
		data.put("type", 10);
		data.put("status", 200);
		JSONObject params = new JSONObject();
		params.put("messageId", groupMessageRequestPacket.getMessageId());
		params.put("message", groupMessageRequestPacket.getMessage());
		params.put("fileType", groupMessageRequestPacket.getFileType());
		params.put("fromUser", fromUser);
		params.put("groupId", groupMessageRequestPacket.getToGroupId());
		params.put("groupCount", nameList.size());
		params.put("nameList", nameList);
		data.put("params", params);
		return data.toJSONString();
	}
}
