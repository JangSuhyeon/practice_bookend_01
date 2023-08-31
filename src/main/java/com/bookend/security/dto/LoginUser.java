package com.bookend.security.dto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)  // 메서드의 파라미터에 적용될 수 있도록 설정
@Retention(RetentionPolicy.RUNTIME) // 주로 런타임 중에 메서드 파라미터에 대한 정보를 확인하거나 처리할 때 사용될 것
public @interface LoginUser {
}
