package com.h232ch.demospringsecurityh232ch;

import com.h232ch.demospringsecurityh232ch.account.Account;
import com.h232ch.demospringsecurityh232ch.account.AccountContext;
import com.h232ch.demospringsecurityh232ch.common.SecurityLogger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;
import java.util.Collection;

@Service // 서비스에 principal 정보를 넘겨주지 않더라도 해당 서비스 안에서 Authentication 정보를 활용가능 (ThreadLocal 방식이기 때문에 가능)
public class SampleService { // 파트 10


//    @Secured("ROLE_USER") // 파트 44  메서드를 호출하기 전에 권한검사를 수행
//    @RolesAllowed("ROLE_USER") // 위와 동일한 기능을 수행  메서드를 호출하기 전에 권한검사를 수행
//    @PreAuthorize("hasRole(USER)") // 메서드를 호출하기 전에 권한검사를 수행
//    @PostAuthorize() 메서드 실행 이후에 인가를 수행할 수있음
//    @PreAuthorize("#username == authentication.principal.username")
    public void dashboard() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // SecurityContextHolder에는 인증된 객체의 정보만 들어감
        Object principal = authentication.getPrincipal(); // 인증 사용자를 나타냄 (getPrincipal) 누구냐? (UserDetailsService에서 리턴한 User 객체)
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities(); // 인증사용자가 가지고 있는 권한을 나타냄 (getAuthorities)
        // (UserDetailsService에서 리턴한 User 객체)
        Object credentials = authentication.getCredentials(); // 크리덴셜
        boolean authenticated = authentication.isAuthenticated(); // 인증된 사용자냐? (로그인 상태라면 True)
        // 권한정보는 AccountService에 기재된 UserDetailsService를 기반으로 작성됨
        // ThreadLocal을 사용하는 SpringContextHolder는 최종적으로 Authentication 담고있으며 Application 어디에서든 사용 가능하도록 해줌
        // Authority에는 Princial, GrantAuthority 두개의 정보가 들어있음


    }

    public void dashboard2() { // 파트 12
        Account account = AccountContext.getAccount();
        System.out.println("==================");
        System.out.println(account.getUsername()); // 로컬쓰레드에 저장된 Account를 불러온다
        // SpringSecurityHolder의 경우 위의 기능을 이용하여 자동으로 넣어준다.
    }

    public void dashboard3() { // 파트 13 : SecurityContextHolder에 존재하는 Authentication 값을 UserDetail로 받아와서 User이름을 출력
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal(); 
        System.out.println("==================");
        System.out.println(authentication);
        System.out.println(userDetails.getUsername());

    }

    @Async // 특정 Bean의 메소드를 호출할때 별도의 쓰레드를 만들어서 비동기적으로 호출해줌
    // Async만 사용한 경우 SecurityContextHolder의 Authentication이 공유가 안됨으로 설정이 필요함
    public void asyncService() { // 파트 23
        SecurityLogger.log("Async Service");
        System.out.println("Async Service is called");
    }
}
