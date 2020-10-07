package com.h232ch.demospringsecurityh232ch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableAsync // 파트 24
public class DemoSpringSecurityH232chApplication {

    @Bean
    public PasswordEncoder passwordEncoder(){
//        return NoOpPasswordEncoder.getInstance(); // 패스워드 인코딩을 하지 않음 (Spring 5이전 버전에서 사용) 이것을 사용하면 Password에 Prefix 설정을 안해도 평문으로 사용 가능
        return PasswordEncoderFactories.createDelegatingPasswordEncoder(); // Spring5 이후 버전에서 사용 기본적으로 Bcrypt가 지원됨
    }


    public static void main(String[] args) {
        SpringApplication.run(DemoSpringSecurityH232chApplication.class, args);
    }

}
