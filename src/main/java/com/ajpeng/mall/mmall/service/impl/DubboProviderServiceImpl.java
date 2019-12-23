package com.ajpeng.mall.mmall.service.impl;

import com.ajpeng.mall.mmall.service.DubboProviderService;

public class DubboProviderServiceImpl implements DubboProviderService {
    @Override
    public String sayHello(String name) {
        return "服务员001";
    }
}
