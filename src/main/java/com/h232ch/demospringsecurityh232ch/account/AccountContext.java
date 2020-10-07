package com.h232ch.demospringsecurityh232ch.account;

public class AccountContext { // 파트 12

    // 인증을 위해 Authentication을 이용함 실제 인증은 AuthenticationManager 인터페이스의 구현제인 ProviderManager에서 구현되며
    // Authentication은 SecurityContext를 통해 사용가능하며 이는 SecurityContextHolder가 제공한다.

    // 인증정보는 SpringSecurityHolder를 통해 가져오고
    // SecurityContextHolder의 기본 전략은 ThreadLocal이다. -> 우리의 인증정보는 ThreadLocal 형태로 가져오는 것이다!
    // 즉 Authentication 객체 ThreadLocal을 통해 넣어주는 것
    // 이후 SecurityContextHolder에 들어가서 애플리케이션 전반에 걸처 우리가 사용할 수 있도록 함

    // ThreadLocal은 프로그램에서 변수를 전역으로 사용가능하게 한다.
    // ThreadLocal은 아래 예제와 같이 구현되어 있다!

    private static final ThreadLocal<Account> ACCOUNT_THREAD_LOCAL
            = new ThreadLocal<>();
    public static void setAccount(Account account){
        ACCOUNT_THREAD_LOCAL.set(account);
    }

    public static Account getAccount(){
        return ACCOUNT_THREAD_LOCAL.get();
    }
}
