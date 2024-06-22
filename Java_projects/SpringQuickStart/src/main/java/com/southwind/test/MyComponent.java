package com.southwind.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//注解会被保存在运行时
@Retention(RetentionPolicy.RUNTIME)
//TYPE是给类加的
@Target(ElementType.TYPE)
public @interface MyComponent {
}
