package com.example.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME) // 유지기간 설정
public @interface Controller {
    // 어노테이션 호출 시 사용할 기본 값 정의
    String value() default ""; // value의 기본 값은 ""
    String path() default ""; // path의 기본 값은 ""
}
