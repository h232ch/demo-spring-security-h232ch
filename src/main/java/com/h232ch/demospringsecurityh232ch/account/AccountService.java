package com.h232ch.demospringsecurityh232ch.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements UserDetailsService { // UserDetailsService는 SpringSecurity에서 Authentication을 관리할 때
    // DAO(Data Access Object)인터페이스를 통해서 저장소에 저장되어있는 유저 정보를 읽어오는 인터페이스이다.
    // 이 인터페이스를 임플레먼트하는 클래스를 빈으로등록만 하면 SpringSecurity가 자동으로 해당 Service를 UserDetailService로 사용함

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AccountRepository accountRepository; // 만약 JPA를 사용하지 않는다면 여기에 DB를 연결하는 DAO 구현체가 와야함

    @Override // 스프링 시큐리티로 유저 정보를 제공하는 역할 -> 실제 인증을 하는 인터페이스는 AuthentificationManager가 수행
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException { // username을 받아와서 username이 해당하는 user정보를 가져와서 userDetails 타입으로 리턴하는 역할
        Account account = accountRepository.findByUsername(s); // 여기서 User를 꺼내오겠다.
        if(account == null){
            throw new UsernameNotFoundException(s);
            
        }
//        return User.builder() // SpringSecurity에서 제공하는 User빌더 (Account형의 User정보를 UserDetails형에 맞도록 User Builder를 통해 생성하여 리턴 (Builder를 사용한다는건 Lombok을 쓴다는것)
//                .username(account.getUsername())
//                .password(account.getPassword())
//                .roles(account.getRole()) // , "USER") 로 권한을 추가할 수도 있음 파트 16
//                .build();

        return new UserAccount(account); // 파트 45 build를 사용하지 않고 UserAccount로 객체를 만들어 쓰기
        // 위의 값은 SecurityContextHolder에 principal로 저장 이 principal을 @AuthenticationPrincipal로 받아와서 사용함
    }

    public Account createNew(Account account) {

//        account.encodePassword(); // 입력받은 패스워드 앞에 Prefix를 설정하고 리턴함 
        account.encodePassword(passwordEncoder); // Password Encoder 사용시 passwordEncoder를 매개변수로 받아야 함
        return this.accountRepository.save(account);

    }
}
