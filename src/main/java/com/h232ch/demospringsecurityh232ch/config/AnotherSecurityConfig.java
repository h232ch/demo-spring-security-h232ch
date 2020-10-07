package com.h232ch.demospringsecurityh232ch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Order(Ordered.LOWEST_PRECEDENCE - 100) // SecurityConfig 우선순위 정해주기 파트 14
//@EnableWebSecurity // 부트가 자동으로 설정해주기 때문에 빼도됨
public class AnotherSecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/acount/**") // 옵션에 따라 FilterChainProxy가 필요한 필터를 선택함
                .authorizeRequests()
                .anyRequest().permitAll(); // 전부다 인증을 요구하겠다.
    }

}