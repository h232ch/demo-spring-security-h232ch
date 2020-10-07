package com.h232ch.demospringsecurityh232ch;

import com.h232ch.demospringsecurityh232ch.account.Account;
import com.h232ch.demospringsecurityh232ch.account.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    SampleService sampleService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Test
//    @WithMockUser // 테스트가 목적이라면 WithMockUser로 가짜객체가 있는것처럼 동작하게 할수있음
    public void dashboard(){
        Account account = new Account();
//        account.setRole("ADMIN"); // 허용이 안됨
        account.setRole("ADMIN");
        account.setUsername("sh");
        account.setPassword("123");

        accountService.createNew(account); // sh 계정 생성
        UserDetails userDetails = accountService.loadUserByUsername("sh"); // UserDetails 타입으로 받아오기

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, "123"); // 인증토큰 생성
        Authentication authenticate = authenticationManager.authenticate(token); // 인증 수행

        SecurityContextHolder.getContext().setAuthentication(authenticate);
        sampleService.dashboard();
    }

}