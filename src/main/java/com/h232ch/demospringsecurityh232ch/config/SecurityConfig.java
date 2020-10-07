package com.h232ch.demospringsecurityh232ch.config;

import com.h232ch.demospringsecurityh232ch.account.AccountService;
import com.h232ch.demospringsecurityh232ch.common.LoggingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Configuration
@Order(Ordered.LOWEST_PRECEDENCE - 50) // 숫자가 낮을수록 우선순위가 높음
//@EnableWebSecurity // 부트가 자동으로 설정해주기 때문에 빼도됨
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    AccountService accountService;

    public AccessDecisionManager accessDecisionManager(){ // 계층구조의 롤을 설정하기 위함, 롤히어라키 세팅을  configure에서 지원안함
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");

        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy);

        WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
        webExpressionVoter.setExpressionHandler(handler);

        List<AccessDecisionVoter<? extends Object>> voters = Arrays.asList(webExpressionVoter);
        return new AffirmativeBased(voters);

        // WebDecisionManager는 인가를 담당하고 있으며 내부에 Access Control 결정을 내리는 AffimativeBased와
        // 해당 Authentication이 특정한 Obejct에 접근할 때 필요한 ConfigAttributes를 만족하는지 확인하는 AccessDecitinVoter를 사용한다.
        // 우리는 WebExpressionVoter가 사용하는 handler에 SetRoleHierachy를 해주기 위해 AccessDecisionManager를 직접 구현한 것임
        // congifure에서 해당 설정이 없다고 함

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http.antMatcher("/**") // Oder보다 AntMatcher를 사용하여 적절히 설정하는게 좋음
                .authorizeRequests() // 이 설정이 파트 39의 FilterSecurityInterceptor 이다.
                .mvcMatchers("/","info","/account/**", "signup").permitAll() // /, info 정보는 누구나 볼수있음
                .mvcMatchers("/admin").hasRole("ADMIN") // admin 페이지 접속시 ADMIN Role을 가지고 있어야함
                .mvcMatchers("/user").hasRole("USER") // 파트 16 USER 권한을 갖는 사용자가 USER 페이지 접속
                .anyRequest().authenticated() // 기타 다른 요청은 모두 로그인 필요
//                .anyRequest().rememberMe() // RememberMe를 사용하여 인증한 사용자에게 접근권한을 주겠다. (내 로그인을 기억)
//                .anyRequest().fullyAuthenticated() // RememberMe를 사용하여 인증한 사용자도 다시한번 로그인을 요청함 (중요 URL)
//                .anyRequest().anonymous() // 인증을 수행하고 접근하면 접근이 안됨 (anonymous만 접근가능)
                .accessDecisionManager(accessDecisionManager()) // 우리가 설정한 AccessDicisionManager를 사용하게 함
//                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // 파트 12 Static 리소스의 스프링 시큐리티 미적용 두번째 방법
                // 위 방법은 추천하지 않음 -> 스태틱 리소스의 필터 15개를 다 태움 -> 성능상의 문제
                .and()// and를 굳이 쓰지 않아도 됨
                .formLogin()
                .and()
                .httpBasic(); // and 뒤에 오는것은 아래와 같이 별도로 사용가능
                // 파트 33 http basic 인증이란 요청해더에 username, password를 실어 보내면 브라우저 또는 서버가 그 값을 읽어서 인증함
                // AUthorization: Basic SDifuSkjfjgoasklDFu9i90 (username:password를 Base64로 인코딩한 것)
                // 위 인증을 사용할 때는 반드시 HTTPS를 사용해야 함

        //        http.csrf().disable(); // CSRF 끄기 옵션 파트 27
//        http.formLogin(); // authentificatied 요청시 form 로긴 페이지를 보여줌

        http.logout()
                .logoutUrl("/logout") // 파트 32
                .logoutSuccessUrl("/"); // 파트 29 로그아웃시 리다이렉션 페이지 커스텀
//                .invalidateHttpSession(true) // 로그아웃 후 HttpSession을 삭제 (기본값이 True라서 별도의 커스텀 불필요
//                .logoutSuccessHandler() // 로그아웃 완료시 별도의 핸들러 작업 수행
//                .addLogoutHandler() // 로그아웃시 부과적인 작업 수행
//                .deleteCookies() // 쿠키 기반의 로그인 사용시 로그아웃시 쿠키 삭제

        http.formLogin() // 파트 31, 32
            .loginPage("/login") // 로그인 페이지를 별도로 만들겠다. 이런경우 DefaultLoginPageGenerateFilter + LogoutFilter가 적용되지 않음
            .permitAll(); // 로그인 페이제에 PermitAll 설정이 필수적으로 필요함 (안할 경우 페이지가 안보임)
        // Form 로긴의 경우 기본적으로 세션유지(Stateful)하게 사용함 (BasicAuthentication을 사용할 경우 http Basic 요청을 수행 (Authorization)
//            .usernameParameter("my-username"); // 파라메터 이름을 커스텀할 수 있는데 이렇게 하면 프론트 단에서도 동일하게 맍춰줘야 함
//                .usernameParameter("my-sh") // username 파라메터명을 변경함
//                .passwordParameter("my-password"); // password 파라메터명을 변경함
        
//        http.anonymous().principal("annoymouse_user"); // 파트 36 기본값은 annonymouseUser, Role값 등 변경 가능 굳이 쓸 필요가 없다
//        사용자 로그인 정보가 없다면 annonymouse token을 생성해서 넣어줌 Null Object Pattern( Null을 대변하는 객체를 넣어서 사용하는 패턴 )


        http.rememberMe() // 파트 39
                .rememberMeParameter("resession") // login 페이지의 파라메터명도 변경해줘야함
//                .alwaysRemember() // remember-me를 설정하지 않아도 기본으로 remember-me를 사용하겠다.
//                .useSecureCookie() // https에서만 적용 (https 적용후)
//                .tokenValiditySeconds() // 기본값이 2주
                .userDetailsService(accountService) // Jsession이 삭제되어도 Remeber-me가 설정되어있어서 로그인상태 유지
                .key("remember-me-sample");


        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL); // 파트 24
        // 톰캣에서 기본으로 생성하는 NIO 쓰레드 하위 쓰레드(Task)에 SecurityContextHolder를 사용할 수 있도록 하는 옵션


//        http.sessionManagement() // 파트 37
//                .sessionFixation() // 세션방지 전략 설정 기본값은 changeSessionId 이다.
//                .changeSessionId() // 굳이 바꿀필요가 없다 이유 migrateSessionId보다 ChnageSessionId가 효율적이다.
//        .invalidSessionUrl("/login") // 유효하지 않은 세션이 접근했을때 어디로 보낼것인가?
//        .maximumSessions(1) // 1개의 계정에 대해 1개의 세션만 유지하겠다 (동시접속 불가)
//        .expiredUrl("/login") // 만약 1개의 계정에 2개의 세션이 생성되면 기존 세션은 expired되고 expiredURL으로 리디렉션됨
//        .maxSessionsPreventsLogin(true); // 기본값은 False, 1개의 계정에 2개의 세션이 생성되면 새로운 새션의 로그인을 막는다.

//        http.sessionManagement() // 파트 37
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 세션을 사용하지 않음 (로그인 후 인증이 유지안됨)
        // 여러 서버간의 세션공유를 위한 프로젝트 spring session (추후 찾아보기)


        // TODO ExceptionTranslatorFilter ->(다음에 오는 필터) FilterSecurityInterceptor
        // FilterSecurityInterceptor 전에 ExceptionTranslatorFilter가 있어야함
        // ExceptionTranslatorFilter에서 Try/Catch로 감싸고 그 다음 ExceptionTranslatorFilter를 처리함
        // ExceptionTranslatorFilter (AccessDecisionManager의 구현체인 AffirmativeBased)는 인가처리를 수행함 (권한이 필요한 리소스 접근시 인가)
        // 이때 두가지 예외가 발생할 수 있음
        // 1. AuthenticationException 관련 에러 (인증이안되어있다) -> AuthenticationEntryPoint를 사용하여 예외 처리 (해당 User를 인증페이지로 보냄)
        // 2. AccessDeniedException 관련 에러 (인증은 되어있으나 충분한 권한이 없다) -> AccessDeniedHandler (403 에러 메세지를 보여줌)
        // 굳이 커스터마이징 할 필요는 없으나 AccessDeniedException의 경우 페이지 변경 가능

        http.exceptionHandling() // 파트 38
//                .accessDeniedPage("/access-denied"); // User 친화적인 access-denied 페이지 작성후 access denied 발생시 해당 페이지로 이동
                    .accessDeniedHandler(new AccessDeniedHandler() {
                        @Override
                        public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                            UserDetails principal = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                            String username = principal.getUsername();
                            System.out.println(username + " is denied to access " + request.getRequestURI());
                            response.sendRedirect("/access-denied");
                        }
                    });

        // access-denied 페이지를 변경하면서 access-denied 로그까지 남기는 역할을 함
        // 실제 서비스에서는 위의 코드를 별도로 빼고 로거를 사용하여 남겨야 함




        http.addFilterBefore(new LoggingFilter(), WebAsyncManagerIntegrationFilter.class); // 파트 41 커스텀 필터
        // 제일 앞에있는 필터 전에 LoggingFilter를 배치



    }

//    @Override // JPA를 사용하면서 In-Memory 계정정보는 더이상 필요없음으로 주석처리 // ST1
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("sh").password("{noop}123").roles("USER").and() // 현재 DB는 구성하지 않았으나 해당값이 DB값이라고 보면 됨
//                .withUser("admin").password("{noop}123").roles("ADMIN"); // noop prefix의 경우 암호화를 하지 않고 비교하는 것
//                // 보통은 prefix에 따라 사용자가 입력한 패스워드를 암호화하여 저장된 데이터와 일치하면 인증을 해줌
//    }




    @Override
    public void configure(WebSecurity web) throws Exception { // 파트 21 특정 요청에 대한 Spring Security 설정을 거부하고 싶을때
//        web.ignoring().mvcMatchers("/favicon.ico"); // 파비콘 요청을 무시하겠다. 하지만 매번 스태틱 리소스를 명시하는건 귀찮은일
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations()); // 이와 같이 설정하면 스태틱 리소스에 대해 Spring Security 예외 처리함
        // ignoring을 적용할 경우 Spring Security Filter 자체를 0으로 만듦 (FilterProxy에서 Filter를 가져오지 않음)
        // atCommonLocation 뒤에 추가로 기재하여 구체적인 통제가 가능하다.
        // 방법은 다양함
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception { // 파트 44 메소트 시큐리티
        return super.authenticationManagerBean(); // Test 코드를 위해 만들어 줘야함 AuthenticationManager 빈은 자동등록되지 않아서 이렇게 등록해줘야함
    }
}
