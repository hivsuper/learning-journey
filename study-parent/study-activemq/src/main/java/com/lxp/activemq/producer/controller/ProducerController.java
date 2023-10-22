package com.lxp.activemq.producer.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.inject.Inject;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lxp.activemq.producer.service.MessageService;

@Controller
@RequestMapping(value = "/producer")
public class ProducerController {
    @Inject
    private ActiveMQQueue queueDestination;
    @Inject
    private MessageService messageService;

    @ResponseBody
    @RequestMapping(value = "/sendAString", method = POST)
    public void sendAString(@RequestParam String content) {
        messageService.sendTextMessage(queueDestination, content);
    }

    @ResponseBody
    @RequestMapping(value = "/sendAObject", method = POST)
    public void sendAObject(@RequestParam String sender, @RequestParam String receiver, @RequestParam String content) {
        messageService.sendObjectMessage(queueDestination, sender, receiver, content);
    }
}
