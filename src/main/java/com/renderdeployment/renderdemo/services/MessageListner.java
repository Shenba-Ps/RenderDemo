package com.renderdeployment.renderdemo.services;

import com.renderdeployment.renderdemo.config.MessagingConfig;
import com.renderdeployment.renderdemo.dto.MessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageListner {
     Logger logger = LoggerFactory.getLogger(MessageListner.class);

/*    @RabbitListener(queues = MessagingConfig.QUEUE)
    public void consumeMessageFromQueue(MessageDto obj) {
        System.out.println("Message recieved from queue : " + obj);
        logger.info("Message recieved from queue : " + obj);
    }*/
}
