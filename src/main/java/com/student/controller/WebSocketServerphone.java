package com.student.controller;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author:wf
 * @Date 2023/5/14 13:55
 * 消息收发
 **/
@Controller
@ServerEndpoint(value = "/web/video/{senderID}/{recipientId}")
@Slf4j
public class WebSocketServerphone {


    /** 当前在线连接数。应该把它设计成线程安全的 */
    private static  int onlineCount = 0;
    /** 存放每个客户端对应的MyWebSocket对象。实现服务端与单一客户端通信的话，其中Key可以为用户标识 */
    private static ConcurrentHashMap<String, Session> webSocketSet = new ConcurrentHashMap<String, Session>();

    /** 与某个客户端的连接会话，需要通过它来给客户端发送数据 */
    private Session WebSocketsession;
    /** 当前发消息的人员编号 */
    private String senderID = "";


    /**
     * 连接建立成功调用的方法
     * @param param 发送者ID，是由谁发送的
     * @param WebSocketsession 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(@PathParam(value = "senderID") String param, @PathParam(value = "recipientId") String recipientId,Session WebSocketsession) {
        System.out.println("人员-------**-------编号:"+param+"：加入聊天");
        System.out.println("盆友是:"+recipientId+"");

        //接收到发送消息的人员编号
        senderID = param;
        System.out.println("senderID："+senderID);
        //设置消息大小最大为10M，这种方式也可以达到效果，或者使用下面的    @OnMessage(maxMessageSize=5242880)
        //The default buffer size for text messages is 8192 bytes.消息超过8192b,自动断开连接

//        WebSocketsession.setMaxTextMessageBufferSize(10*1024*1024);
//        WebSocketsession.setMaxBinaryMessageBufferSize(10*1024*1024);


        //加入map中，绑定当前用户和socket
        webSocketSet.put(param, WebSocketsession);
        //在线数加1
        addOnlineCount();
    }


    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (StrUtil.isNotBlank(senderID)) {
            //从set中删除
            webSocketSet.remove(senderID);
            //在线数减1
            subOnlineCount();
        }
    }


    /**
     * 收到客户端消息后调用的方法
     *
     *设置最大接收消息大小
     */
    @OnMessage(maxMessageSize=5242880)
    public void onMessage(@PathParam(value = "senderID") String senderID ,@PathParam(value = "recipientId") String recipientId,InputStream inputStream) {
        System.out.println(senderID+":发送给"+recipientId+"的消息-->"+inputStream);

        try {
            byte[] buff = new byte[inputStream.available()];
            inputStream.read(buff, 0, inputStream.available());
            Session session = webSocketSet.get(recipientId);
            synchronized (session) {
                //给2号发送
                session.getBasicRemote().sendBinary(ByteBuffer.wrap(buff));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }


    /**
     * 为指定用户发送消息
     *
     * @param message 消息内容
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        //加同步锁，解决多线程下发送消息异常关闭
        synchronized (this.WebSocketsession){
            this.WebSocketsession.getBasicRemote().sendText(message);
        }
    }

    /**
     * 获取当前在线人数
     * @return 返回当前在线人数
     */
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    /**
     * 增加当前在线人数
     */
    public static synchronized void addOnlineCount() {
        WebSocketServerphone.onlineCount++;
    }

    /**
     * 减少当前在线人数
     */
    public static synchronized void subOnlineCount() {
        WebSocketServerphone.onlineCount--;
    }

}
