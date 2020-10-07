package com.h232ch.demospringsecurityh232ch.account;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

public class UserAccount extends User { // 파트 45 스프링 시큐리티가 제공하는 User를 상속받고 생성자를 상속받아 사용

    private Account account;
    // principal에 UserAccount가 생성되고 UserAccount 내에 account 값이 생성됨

    public Account getAccount() {
        return account;
    }

    public UserAccount(Account account) {
        super(account.getUsername(), account.getPassword(),  List.of(new SimpleGrantedAuthority("ROLE_"+account.getRole())));
        // User 생석자를 통해 id, username, pssword, role 이 UserAccount 객체에 기록됨
        this.account=account;
        //
    }
}
