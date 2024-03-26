package com.student.netty.serve;

import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.net.URI;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
//@Component
//@DependsOn(value = {"INettyServer"})
public class INettyClient implements InitializingBean {

    @Value("${netty.port}")
    private int nettyPort;

    @Resource
    private RBlockingQueue<String> blockingQueue;
    final CountDownLatch latch = new CountDownLatch(1);

    ExecutorService executorService = Executors.newFixedThreadPool(1);

    @Override
    public void afterPropertiesSet() throws Exception {
        String url = "ws://127.0.0.1:"+ nettyPort +"/socket.io";
        final URI webSocketURL = new URI(url);
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap boot = new Bootstrap();
        try{
            boot.option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .group(group)
                    //debug调试打印信息 测试
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel sc) throws Exception {
                            ChannelPipeline pipeline = sc.pipeline();
                            pipeline.addLast(new HttpClientCodec());
                            pipeline.addLast(new ChunkedWriteHandler());
                            pipeline.addLast(new HttpObjectAggregator(1024 * 1024));
                            pipeline.addLast(new WebSocketClientProtocolHandler(WebSocketClientHandshakerFactory.newHandshaker(webSocketURL, WebSocketVersion.V13, null, false, new DefaultHttpHeaders(), 1024 * 1024 * 50)));
                            pipeline.addLast(new SimpleChannelInboundHandler<TextWebSocketFrame>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
                                    System.err.println(" 客户端收到消息：" + msg.text());
                                }

                                @Override
                                public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                                    if (WebSocketClientProtocolHandler.ClientHandshakeStateEvent.HANDSHAKE_COMPLETE.equals(evt)) {
                                        log.info(ctx.channel().id().asShortText() + " 握手完成！");
                                        latch.countDown();
                                    }
                                    super.userEventTriggered(ctx, evt);
                                }

                                @Override
                                public void channelInactive(ChannelHandlerContext ctx){
                                    comunmier(ctx.channel());
                                    ctx.channel().close();
                                    log.info("连接关闭");
                                }
                            });
                        }
                    });

            ChannelFuture cf = boot.connect(webSocketURL.getHost(), webSocketURL.getPort()).sync();
            //异步等待握手成功
            latch.await();
            //消费
            executorService.submit(() -> {
                while (true) {
                    try {
                        String json = blockingQueue.take();
                        log.info("正在消费的JSON-时间：{}, 数据:{}", new Date(System.currentTimeMillis()), json);
                        send(cf.channel(), json);
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }finally {
                        //关闭时清空 或者关闭前强行在某处调用完毕
                        comunmier(cf.channel());
                    }
                }
            });

            //心跳保活
            Thread heart = new Thread(() -> {
                while (true) {
                    try {
                        JSONObject res = new JSONObject();
                        res.put("type", 11);
                        JSONObject params = new JSONObject();
                        res.put("params", params);
                        String json = res.toJSONString();
                        cf.channel().writeAndFlush(new TextWebSocketFrame(json));
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, "heartThread");
            heart.start();
        }catch (Exception e){
            group.shutdownGracefully();
            e.printStackTrace();
        }
    }


    private void comunmier(Channel channel) {
        String q = null;
        while ((q = blockingQueue.poll()) != null) {
            send(channel, q);
        }
        blockingQueue.clear();
    }

    public static void send(Channel channel, String textMsg) {
        if (channel != null && channel.isActive()) {
            TextWebSocketFrame frame = new TextWebSocketFrame(textMsg);
            channel.writeAndFlush(frame).addListener((ChannelFutureListener) channelFuture -> {
                if (channelFuture.isDone() && channelFuture.isSuccess()) {
                    log.info("发送成功");
                } else {
                    channelFuture.channel().close();
                    log.info("发送失败! cause:{ %s }", channelFuture.cause());
                    channelFuture.cause().printStackTrace();
                }
            });
        } else {
            log.error("消息发送失败！ textMsg:{}",textMsg);
        }
    }
}
