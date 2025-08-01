package com.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.TYPE})
public @interface RequestMapping {

    String value() default "";

    RequestMethod[] method() default  {};
}
