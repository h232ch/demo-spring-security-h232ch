package com.h232ch.demospringsecurityh232ch.account;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class AccessDeniedController { // 파트 38

    @GetMapping("/access-denied")
    public String accessDenied(Principal principal, Model model){
        model.addAttribute("name", principal.getName());
        return "access-denied";
    }
}
