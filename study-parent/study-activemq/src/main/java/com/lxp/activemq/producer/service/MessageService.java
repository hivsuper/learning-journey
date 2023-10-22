package com.lxp.activemq.producer.service;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.lxp.activemq.common.model.CustomMessage;

@Service
public class MessageService {
    private static final Logger LOG = LoggerFactory.getLogger(MessageService.class);
    @Inject
    private JmsTemplate jmsTemplate;

    public void sendTextMessage(Destination destination, final String content) {
        LOG.info("send content={}", content);
        /**
         * input a message without converter
         */
        jmsTemplate.send(destination, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(content);
            }
        });
    }

    public void sendObjectMessage(Destination destination, String sender, String receiver, String content) {
        LOG.info("{} send content={} to {}", sender, content, receiver);
        final CustomMessage message = new CustomMessage();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);

        Calendar calendar = Calendar.getInstance();
        Date effectiveTime = calendar.getTime();
        calendar.add(Calendar.MINUTE, 5);
        Date expiredTime = calendar.getTime();

        message.setEffectiveTime(effectiveTime);
        message.setExpiredTime(expiredTime);
        /**
         * input a message with converter
         */
        jmsTemplate.convertAndSend(destination, message);
    }
}
