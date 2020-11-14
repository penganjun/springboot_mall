package com.ajpeng.mall.mmall.service.impl;

import com.ajpeng.mall.mmall.entity.SysUser;
import com.ajpeng.mall.mmall.mapper.UserMapper;
import com.ajpeng.mall.mmall.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private Logger logger = LoggerFactory.getLogger(getClass());

   @Autowired
   private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = userMapper.findByUsername(username);
        return sysUser;
    }
}
