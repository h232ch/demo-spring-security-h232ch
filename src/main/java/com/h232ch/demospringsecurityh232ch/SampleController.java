package com.h232ch.demospringsecurityh232ch;

import com.h232ch.demospringsecurityh232ch.account.Account;
import com.h232ch.demospringsecurityh232ch.account.AccountContext;
import com.h232ch.demospringsecurityh232ch.account.AccountRepository;
import com.h232ch.demospringsecurityh232ch.account.UserAccount;
import com.h232ch.demospringsecurityh232ch.book.BookRepository;
import com.h232ch.demospringsecurityh232ch.common.CurrentUser;
import com.h232ch.demospringsecurityh232ch.common.SecurityLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.concurrent.Callable;

@Controller
public class SampleController {


    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SampleService sampleService; // 파트 10

    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/")
//    public String index(Model model, Principal principal){ // 스프링에서 제공하는 아규먼트 리졸버인 Principal은 SecurityContextHolder의 Principal과 다르다.
    // principal은 제한적이다. 우리는 도메인 타입의 객체를 쓰고싶다. java.security.Principal이 아닌 Spring Security가 제공하는 Principal로 받는 방법이 있다.
    // java.security.Principal의 경우 아규먼트 리졸버로서 SecurityContextHolder의 Principal을 리졸버하는데 해당 기능이 한정적으로
    // Account의 본연의 정보를 다 활용하고자 할때 SpringSecurityContextHolder의 Principal을 써야함
//    public String index(Model model, @AuthenticationPrincipal UserAccount userAccount){ // 파트 45 자바 Security Principal이 아닌 Spring Security Principal을 받아옴
    public String index(Model model, @CurrentUser Account account){ // @CurrentUser 애노테이션을 사용하여 Account 정보를 가져옴

//        if(principal == null){
//            model.addAttribute("message", "Hello Spring Security"); // Key Value 형태로 모델에 String 객체를 넣어줌
//        } else {
//            model.addAttribute("message", "Hello, " + principal.getName());
//        }

        if(account == null){
            model.addAttribute("message", "Hello Spring Security"); // 파트 45 Spring Security Princial
        } else {
            model.addAttribute("message", "Hello, " + account.getUsername());
        }
        return "index";
    }

    @GetMapping("/index2")
    public String index2(Model model, Principal principal){

        if(principal == null){
            model.addAttribute("message", "Hello Spring Security"); // Key Value 형태로 모델에 String 객체를 넣어줌
        } else {
            model.addAttribute("message", "Hello, " + principal.getName());
        }
        return "index2";
    }

    @GetMapping("/info")
    public String info(Model model){
        model.addAttribute("message", "Hello Spring Security"); // Key Value 형태로 모델에 String 객체를 넣어줌
        return "info";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal){
        model.addAttribute("message", "Hello, " + principal.getName() ); // Key Value 형태로 모델에 String 객체를 넣어줌
        return "dashboard";
    }

    @GetMapping("/admin")
    public String admin(Model model, Principal principal){
        model.addAttribute("message", "Hello, Admin " + principal.getName()); // Key Value 형태로 모델에 String 객체를 넣어줌
        return "admin";
    }


    @GetMapping("/dashboard2")
    public String dashboard2(Model model, Principal principal){ // principal 객체를 받아서 로그인된 사용자의 정보를 알 수 있다.
        sampleService.dashboard(); // 서비스에 principal 정보를 넘겨주지 않더라도 해당 서비스 안에서 Authentication 정보를 활용가능 (ThreadLocal 방식이기 때문에 가능)
        model.addAttribute("message", "Hello, " + principal.getName() ); // Key Value 형태로 모델에 String 객체를 넣어줌
        return "dashboard";
    }


    @GetMapping("/dashboard3") // 파트 12
    public String dashboard3(Model model, Principal principal){
        AccountContext.setAccount(accountRepository.findByUsername(principal.getName())); // 쓰레드로컬에 저장함
        sampleService.dashboard2();
        model.addAttribute("message", "Hello, " + principal.getName() ); // Key Value 형태로 모델에 String 객체를 넣어줌
        return "dashboard";
    }

    @GetMapping("/dashboard4") // 파트 13
    public String dashboard4(Model model, Principal principal){
        AccountContext.setAccount(accountRepository.findByUsername(principal.getName())); 
        model.addAttribute("message", "Hello, " + principal.getName() );
        sampleService.dashboard3();
        return "dashboard"; // 이미 인증된 사용자의 정보는 principal에 존재함
    }


    @GetMapping("/user") // 파트 16
    public String user(Model model, Principal principal){
        model.addAttribute("message", "hello User, " + principal.getName());
        model.addAttribute("books", bookRepository.findCurrentUserBooks()); // 파트 46
        return "user";
    }

    @GetMapping("/async-handler") // 파트 23 (WebAsyncManagerIntegrationFilter)
    // WebMVC의 Async 기능을 사용할 때에도 Spring Security를 적용
    @ResponseBody
    public Callable<String> asyncHanlder(){

        SecurityLogger.log("MVC"); // Tomcat이 할당한 NIO 쓰레드

        return new Callable<String>() {
            @Override // 먼저 핸들러에 요청한 쓰레드의 응답을 내보내고 Callable 내의 프로세스가 완료되면 Return을 보냄
            public String call() throws Exception {
                SecurityLogger.log("Callable");
                return "Async Handlder"; // 별도의 쓰레드에서 동작 but Spring Principal은 동일한것을 확인하기 위한 코드
            }
        };
    }

    @GetMapping("async-service") // 파트 24
    @ResponseBody
    public String asyncService(){ // 비동기 처리에서는 순서가 보장되지 않음
        SecurityLogger.log("MVC Before Async Service");
        sampleService.asyncService(); // 비동기 처리 (해당 메서드의 결과를 기다리지 않고 다른 기능이 실행됨)
        SecurityLogger.log("MVC After Async Service");
        return "Async Service";
        // Async를 프로젝트에 적용하기 위해서는 메인메서드에 @EnableAsync를 적용해줘야함
    }


}
