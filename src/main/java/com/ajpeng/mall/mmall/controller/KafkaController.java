package com.ajpeng.mall.mmall.controller;

import com.ajpeng.mall.mmall.kafka.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@RequestMapping("/kafka")
@Controller
@Slf4j
public class KafkaController {
    @Autowired
    private KafkaProducer kafkaProducer;

    @RequestMapping("/send")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public void sendMsg() {
        kafkaProducer.send("this is a test kafka topic message");
    }
}
