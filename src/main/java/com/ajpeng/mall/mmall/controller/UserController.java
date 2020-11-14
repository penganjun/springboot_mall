package com.ajpeng.mall.mmall.controller;

import com.ajpeng.mall.mmall.entity.Book;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    @RequestMapping(value = "/login")
    public String login() {
        return "redirect:/book/list";
    }
}
