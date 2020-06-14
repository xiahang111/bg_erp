package com.bingo.erp.person.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;


    // 装载BCrypt密码编码器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }

    @Bean
    public FilterRegistrationBean registrationBean(JwtAuthenticationTokenFilter filter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }



    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        //原因是因为springSecurty使用X-Frame-Options防止网页被Frame。所以需要关闭为了让后端的接口管理的swagger页面正常显示
        httpSecurity.headers().frameOptions().disable();

        httpSecurity
                // 由于使用的是JWT，我们这里不需要csrf
                .cors()//新加入,允许跨域
                .and()
                .csrf().disable();


        // 基于token，所以不需要session
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(HttpMethod.POST, "/**").permitAll()

                // 允许对于网站静态资源的无授权访问
                .antMatchers(
                        "/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**",
                        "/swagger-resources/**",
                        "/swagge‌​r-ui.html",
                        "/actuator/**",
                        "/druid/**"
                ).permitAll()
                // 对于获取token的rest api要允许匿名访问
                .antMatchers("/api-person/**",
                        "/creatCode/**",
                        "/file/**"
                ).permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().permitAll();

        // 添加JWT filter
        httpSecurity.addFilterBefore(registrationBean(new JwtAuthenticationTokenFilter()).getFilter(), UsernamePasswordAuthenticationFilter.class);

        // 禁用缓存
        httpSecurity.headers().cacheControl();
    }
}

