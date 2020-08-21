package com.ajpeng.mall.mmall.controller;

import com.ajpeng.mall.mmall.common.Constant;
import com.ajpeng.mall.mmall.common.ImageCode;
import com.ajpeng.mall.mmall.tools.HuTools;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class ValidateCodeController {
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @GetMapping("/code/image")
    public void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletWebRequest servletWebRequest = new ServletWebRequest(request);
        ImageCode imageCode = HuTools.generate(servletWebRequest);
        sessionStrategy.setAttribute(servletWebRequest, Constant.SESSION_KEY, imageCode.getsRand());
        ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
    }
}
