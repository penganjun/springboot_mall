package com.ajpeng.mall.mmall.controller;

import com.ajpeng.mall.mmall.mail.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@Slf4j
@RestController
@RequestMapping("/mail")
public class MailController {
    @Autowired
    private MailService mailService;

    @RequestMapping("/send")
    public void send() throws MessagingException {
        mailService.sendSimpleMail("paj742257823@163.com", "test", "hello world");
    }
}
