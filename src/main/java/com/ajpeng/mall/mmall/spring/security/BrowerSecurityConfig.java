package com.ajpeng.mall.mmall.spring.security;

import com.ajpeng.mall.mmall.validate.ValidateCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class BrowerSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;
    @Autowired
    protected AuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Autowired
    protected AuthenticationFailureHandler myAuthenticationFailureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 解决非thymeleaf的form表单提交被拦截问题
        http.csrf().disable();
        // 登录
        http.formLogin()
                .loginPage("/html/login.html")//定义登录页面，未登录时，访问一个需要登录之后才能访问的接口，会自动跳转到该页面
                .loginProcessingUrl("/book/list")
                .usernameParameter("username")//定义登录时，用户名的 key，默认为 username
                .passwordParameter("password")//定义登录时，用户密码的 key，默认为 password
                .successHandler(myAuthenticationSuccessHandler)                                 // 认证成功回调
                .failureHandler(myAuthenticationFailureHandler)                             // 认证失败回调
                .successForwardUrl("/index")
                .failureUrl("/login?error=true")
                .permitAll();//和表单登录相关的接口统统都直接通过
        //授权
        http.authorizeRequests()        // 开启登录配置
                .anyRequest()               // 表示剩余的其他接口，登录之后就能访问
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/html/login.html").permitAll()
                .and()
                .apply(validateCodeSecurityConfig)
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
                        resp.setContentType("application/json;charset=utf-8");
                        PrintWriter out = resp.getWriter();
                        out.write("logout success");
                        out.flush();
                    }
                }).deleteCookies("JESSIONID")
                .permitAll()
                .and()
                .httpBasic();

        //session管理
        http.sessionManagement()
                .invalidSessionUrl("/login")//session失效后跳转
                .maximumSessions(1).maxSessionsPreventsLogin(true);//单用户登录，如果有一个登录了，同一个用户在其他地方不能登录

        //解决中文乱码问题
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        http.addFilterBefore(filter, CsrfFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/code/image",
                "/images/**",
                "/js/**",
                "/login/**",
                "/resources/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
