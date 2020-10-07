package com.h232ch.demospringsecurityh232ch.account;

import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Account {

    @Id @GeneratedValue // DB에 들어갈떄 자동으로 값이 생성
    private Integer id;

    @Column(unique = true) // username은 유일한 값이 들어가야 함
    private String username;

    private String password;
    private String role;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
//        this.password = "{noop}"+this.password; // Spring Security는 Prefix가 필요하므로 Prefix를 입력패스워드 앞에 붙여줘야 함
        this.password = passwordEncoder.encode(this.password); // PasswordEncoder를 사용할 경우 prefix 불필요 // 기본적으로 Bcrypt 사용
    }
}
