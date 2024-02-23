package com.student.netty.serve;

import com.alibaba.fastjson.JSONObject;
import com.student.netty.protocol.command.HeartBeatRequestPacket;
import com.student.netty.protocol.command.HeartBeatResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

@Sharable
public class HeartBeatRequestHandler extends SimpleChannelInboundHandler<HeartBeatRequestPacket>{

	public static HeartBeatRequestHandler INSTANCE = new HeartBeatRequestHandler();

	private HeartBeatRequestHandler () {

	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HeartBeatRequestPacket heartBeatRequestPacket) throws Exception {

		ByteBuf byteBuf = getBuf(ctx);
		ctx.channel().writeAndFlush(new TextWebSocketFrame(byteBuf));
	}

	public ByteBuf getBuf(ChannelHandlerContext ctx) {
		 ByteBuf byteBuf = ctx.alloc().buffer();
		 JSONObject data = new JSONObject();
		 data.put("type", new HeartBeatResponsePacket().getCommand());
		 data.put("status", 200);
		 byte bytes[] = data.toJSONString().getBytes();
		 byteBuf.writeBytes(bytes);
		 return byteBuf;
	}

}
