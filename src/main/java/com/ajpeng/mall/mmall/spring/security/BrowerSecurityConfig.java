package com.ajpeng.mall.mmall.spring.security;

import com.ajpeng.mall.mmall.config.RsaKeyProperties;
import com.ajpeng.mall.mmall.filter.JwtLoginFilter;
import com.ajpeng.mall.mmall.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
@Configuration
@EnableWebSecurity      //这个注解的意思是这个类是Spring Security的配置类
public class BrowerSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserService userService;

    private final RsaKeyProperties rsaKeyProperties;

    public BrowerSecurityConfig(UserService userService, RsaKeyProperties rsaKeyProperties) {
        this.userService = userService;
        this.rsaKeyProperties = rsaKeyProperties;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 认证用户的来源
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //数据库中
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    /**
     * 配置SpringSecurity相关信息
     *
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()  //关闭csrf
                .addFilter(new JwtLoginFilter(super.authenticationManager(),rsaKeyProperties))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);    //禁用session
    }
}
