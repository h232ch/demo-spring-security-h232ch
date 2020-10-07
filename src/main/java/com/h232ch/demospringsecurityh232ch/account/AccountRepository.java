package com.h232ch.demospringsecurityh232ch.account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    // AccountRespository 인터페이스만 생성해도 구현체를 자동생성하여 빈으로 등록해줌 -> findById 등은 모두 상속받은 JpaRepository에 존재한다.
    Account findByUsername(String username);
}
