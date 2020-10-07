package com.h232ch.demospringsecurityh232ch.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController { //파트 12 쓰레드

    @Autowired
    private AccountService accountService; // 이변수는 이 클래스 내부에서 사용 가능한 스콥을 가지고 있음
    // 쓰레드로컬이라는건 변수를 쓰레드스콥으로 지정하고 한 쓰레드 내에서는 그 변수를 공유하는것
    // 프로그램단위의 쓰레드로컬이 존재하는 경우 해당 쓰레드에 변수를 지정하고 가져다 쓰면됨

    @GetMapping("/account/{role}/{username}/{password}") // Url Path에 들어있는 값 각각 Model에 넣어줌
    public Account createAccount(@ModelAttribute Account account){
        return accountService.createNew(account);
    } // SecurityConfig에서 Account/** Url은 인증이 불필요하도록 설정해야함
}
