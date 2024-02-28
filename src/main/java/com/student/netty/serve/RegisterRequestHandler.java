package com.student.netty.serve;

import com.alibaba.fastjson.JSONObject;
import com.student.netty.protocol.command.RegisterRequestPacket;
import com.student.netty.utils.A;
import com.student.netty.utils.SessionUtils;
import com.student.pojo.vo.User;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;

import static com.student.Constant.RedisKey.CHANNEL_ID_KEY;
import static com.student.Constant.RedisKey.ONLINE_SIGN;

/**
 * 注册 绑定用户channel
 * @author holiday
 * date 2020-11-04
 */
@Sharable
@Slf4j
public class RegisterRequestHandler extends SimpleChannelInboundHandler<RegisterRequestPacket>{

	public static RegisterRequestHandler INSTANCE = new RegisterRequestHandler();

	private RegisterRequestHandler() {

	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RegisterRequestPacket registerRequestPacket) throws Exception {
		User loginUser = registerRequestPacket.getUser();
		//本机注册
		SessionUtils.bindChannel(loginUser, ctx.channel());
		//分布式注册
		SessionUtils.addChannelGroup(ctx.channel());
		A.a.redisService.nSetBinary(CHANNEL_ID_KEY, loginUser.getUserId(), ctx.channel().id());
		//判断登录与否
		if (SessionUtils.hasLogin(ctx.channel())) {
			log.info("本机上该用户已登录");
		} else if ((Boolean) A.a.redisService.get(ONLINE_SIGN + loginUser.getUserId())) {
			log.info("该用户已登录其它服务器");
		}
		//返回消息
		ByteBuf buffer = getByteBuf(ctx, "连接成功", registerRequestPacket);
		ctx.channel().writeAndFlush(new TextWebSocketFrame(buffer));
	}

	public ByteBuf getByteBuf(ChannelHandlerContext ctx, String message, RegisterRequestPacket registerRequestPacket) {
		ByteBuf byteBuf = ctx.alloc().buffer();
		User fromUser = A.a.loginService.queryUserById(registerRequestPacket.getUser().getUserId());
		JSONObject data = new JSONObject();
		data.put("type", 2);
		data.put("status", 200);
		JSONObject params = new JSONObject();
		params.put("message", message);
		params.put("fromUser", fromUser);
		data.put("params", params);
		byte []bytes = data.toJSONString().getBytes(Charset.forName("utf-8"));
		byteBuf.writeBytes(bytes);
		return byteBuf;
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		String userId = SessionUtils.getUser(ctx.channel()).getUserId();
		if (StringUtils.isNotBlank(userId)) {
			A.a.redisService.nDelBinary(CHANNEL_ID_KEY, userId);
			//上线标记
			A.a.redisService.del(ONLINE_SIGN + userId);
		}
		SessionUtils.unbind(ctx.channel());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		log.error("{} 异常断开，异常信息 {}", ctx.channel(), cause.getMessage());
		ctx.channel().close();
		cause.printStackTrace();
	}

}
