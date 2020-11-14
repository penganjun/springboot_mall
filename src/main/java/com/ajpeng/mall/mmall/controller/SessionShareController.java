package com.ajpeng.mall.mmall.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/session")
public class SessionShareController {

    @RequestMapping(value = "/getSession")
    public Object getSession(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("sessionId", request.getSession().getId());
        map.put("message", request.getSession().getAttribute("message"));
        map.put("user", request.getSession().getAttribute("user"));
        return map;
    }


    @RequestMapping(value = "/login")
    public String login(HttpServletRequest request, String userName, String password) {
        String msg = "logon failure!";
        if (userName != null && "admin".equals(userName) && "123".equals(password)) {
            request.getSession().setAttribute("user", userName);
            request.getSession().setAttribute("message", request.getRequestURL());
            msg = "login successful!";
        }
        return msg;
    }
}
