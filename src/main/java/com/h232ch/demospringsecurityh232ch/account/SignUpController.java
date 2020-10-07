package com.h232ch.demospringsecurityh232ch.account;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignUpController { // 파트 28


    @Autowired
    AccountService accountService;

    @GetMapping
    public String signupFrom(Model model){
        model.addAttribute("account", new Account());
        return "signup";
    }

    @PostMapping
    public String processSignup(@ModelAttribute Account account) {
        account.setRole("USER");
        accountService.createNew(account);
        return "redirect:/";
    }
}
