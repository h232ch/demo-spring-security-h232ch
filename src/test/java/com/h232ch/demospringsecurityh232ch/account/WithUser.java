package com.h232ch.demospringsecurityh232ch.account;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME) // 런타임 종료전까지 유지
@WithMockUser(username="sh", roles = "USER")
public @interface WithUser {
}
