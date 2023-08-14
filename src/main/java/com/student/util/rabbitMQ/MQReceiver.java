package com.student.util.rabbitMQ;

import com.student.service.SubjectService;
import com.student.service.impl.SubjectServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    private SubjectService subjectService;

    @RabbitListener(queues = {"choosePlan"})
    @RabbitHandler
    public void receiverMsg(String msg){

        LOGGER.info("消费消息{}", msg);
        //解析消息
        String[] strings = msg.split("-");
        String subId =strings[0];
        String stuName = strings[1];

        //执行选课操作
        subjectService.executeChoose(subId, stuName);
    }

}

