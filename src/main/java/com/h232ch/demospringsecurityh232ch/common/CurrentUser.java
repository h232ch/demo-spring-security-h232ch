package com.h232ch.demospringsecurityh232ch.common;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account")
// AuthenticationPrincipal은 SecurityContextHolder에 저장되어 있는 Principal 값을 가져옴
// Principal은 User 생성 아답터 역할을 하는 UserAccount로 리턴을 받아 Principal에 자정함
// expression을 사용하면 UserAccount에 존재하는 account를 Principal로 변환해줌
// 결국 Principal인 UserAccount를 UserAccount내에 존재하는 account로 변환하여 최종적으로 account가 Principal의 값이되며
// 이 값은 java에서 애트리뷰트 리졸버로 사용되는 principal 값이 아닌 SecurityContextHolder에 저장되어있는 Principal으로서
// Account의 모든 기능을 자유롭게 사용할 수 있다.
public @interface CurrentUser {
}
