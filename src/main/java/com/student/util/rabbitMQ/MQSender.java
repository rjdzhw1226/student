package com.student.util.rabbitMQ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MQSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(MQSender.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String sudId, String stuName){

        String msg = sudId+"-"+stuName;
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString().replace("-", ""));
        LOGGER.info("生产者发送消息 {}", msg);

        rabbitTemplate.convertAndSend("amq.direct", "choosePlan_routingKey",
                msg, correlationData);
    }
}
