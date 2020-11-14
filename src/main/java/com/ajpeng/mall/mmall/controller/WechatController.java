package com.ajpeng.mall.mmall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller("/wechat")
public class WechatController {
    @RequestMapping("/scanLogin")
    public ModelAndView root() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        mav.addObject("title", "扫码登录测试");
        return mav;
    }
}
