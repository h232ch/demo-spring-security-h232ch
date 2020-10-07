package com.h232ch.demospringsecurityh232ch.account;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginOutController {

    @GetMapping("/login")
    public String LoginForm(){
        return "login";
    }

    @GetMapping("logout")
    public String logoutForm(){
        return "logout";
    }




}
