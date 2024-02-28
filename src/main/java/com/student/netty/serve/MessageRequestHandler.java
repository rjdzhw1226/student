package com.student.netty.serve;

import com.alibaba.fastjson.JSONObject;
import com.student.netty.protocol.command.MessageRequestPacket;
import com.student.netty.utils.A;
import com.student.netty.utils.SessionUtils;
import com.student.pojo.publishPo;
import com.student.pojo.vo.MessageVo;
import com.student.pojo.vo.User;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

import static com.student.Constant.RedisKey.ONLINE_SIGN;

@Sharable
@Slf4j
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket>{

    public static MessageRequestHandler INSTANCE = new MessageRequestHandler();

	private MessageRequestHandler() {
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket messageRequestPacket) throws Exception {

		String message = "";
		Channel toUserChannel = SessionUtils.getChannel(messageRequestPacket.getToUserId());
		if (toUserChannel != null && SessionUtils.hasLogin(toUserChannel)) {
			//本机发送 本机在线
			message = messageRequestPacket.getMessage();
			User toUser = SessionUtils.getUser(toUserChannel);
			String fileType = messageRequestPacket.getFileType();
			//消息落库
			A.a.loginService.saveMessage(MessageVo.builder()
					.message(message)
					.type(messageRequestPacket.getCommand())
					.fromUserId(messageRequestPacket.getFromUserId())
					.toUserId(toUser.getUserId())
					.fileType(fileType)
					.toGroupId(null).build()
			);
			ByteBuf buf = getByteBuf(ctx, message, toUser, fileType);
			toUserChannel.writeAndFlush(new TextWebSocketFrame(buf));
		} else {
			log.error("当前用户："+messageRequestPacket.getToUserId()+"未在本机登录！其它服务器在线标识：{}",message);
			ByteBuf buf = getByteBufRes(ctx, messageRequestPacket);
			String toUserId = messageRequestPacket.getToUserId();
			String fromId = messageRequestPacket.getToUserId();
			publishPo po = new publishPo(1,toUserId,fromId,messageRequestPacket.getMessage(),buf);
			//分布式发送
			A.a.redisService.publish("channel_single", po);
		}
		//刷新在线标识时长
		A.a.redisService.set(ONLINE_SIGN + "_" + messageRequestPacket.getFromUserId(), true, 1800);
	}

	public ByteBuf getByteBuf(ChannelHandlerContext ctx, String message, User toUser, String fileType) {
		ByteBuf byteBuf = ctx.alloc().buffer();
		User fromUser = SessionUtils.getUser(ctx.channel());
		JSONObject data = new JSONObject();
		data.put("type", 2);
		data.put("status", 200);
		JSONObject params = new JSONObject();
		params.put("message", message);
		params.put("fileType", fileType);
		params.put("fromUser", fromUser);
		params.put("toUser", toUser);
		data.put("params", params);
		byte []bytes = data.toJSONString().getBytes(Charset.forName("utf-8"));
		byteBuf.writeBytes(bytes);
		return byteBuf;
	}

	public ByteBuf getByteBufRes(ChannelHandlerContext ctx,MessageRequestPacket messageRequestPacket) {
		ByteBuf byteBuf = ctx.alloc().buffer();
		User fromUser = A.a.loginService.queryUserById(messageRequestPacket.getFromUserId());
		User toUser = A.a.loginService.queryUserById(messageRequestPacket.getToUserId());
		JSONObject data = new JSONObject();
		data.put("type", 2);
		data.put("status", 200);
		JSONObject params = new JSONObject();
		params.put("message", messageRequestPacket.getMessage());
		params.put("fileType", messageRequestPacket.getFileType());
		params.put("fromUser", fromUser);
		params.put("toUser",toUser);
		data.put("params", params);
		byte []bytes = data.toJSONString().getBytes(Charset.forName("utf-8"));
		byteBuf.writeBytes(bytes);
		return byteBuf;
	}
}
