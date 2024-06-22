package com.southwind.entity;

import java.lang.reflect.Method;

public class CilloProcessor {
    public static void process(Object obj) throws Exception{
        Class<?> clazz = obj.getClass();
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods){
            if(method.isAnnotationPresent(cillo.class)){
                cillo cillo_a = method.getAnnotation(cillo.class);
                String msg = cillo_a.value();

                //抵用代用@Cillo 注解的方法，并传递参数
                method.invoke(obj,msg);
            }
        }
    }
}
