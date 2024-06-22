package com.southwind.test;

import com.southwind.entity.Student;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class StudentTest {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Student> studentClass = Student.class;

        //判断有没有注解->MyComponent,有就返回注解，没有就返回null
        MyComponent myComponent = studentClass.getAnnotation(MyComponent.class);
        if(myComponent != null){
            System.out.println(studentClass + "有注解");
            //获取构造器
            Constructor<Student> constructor = studentClass.getConstructor();
            //创建对象
            Student student = constructor.newInstance(null);
            System.out.println(student);

            //获取到类的所有属性，判断注解
            //获取到此类自己的所有修饰符修饰的属性，不包含他的父类
            Field[] declaredFields =  studentClass.getDeclaredFields();
            //遍历属性
            for (Field declaredField : declaredFields) {
                //判断是否添加了MyValue注解
                MyValue myValue = declaredField.getAnnotation(MyValue.class);
                if(myValue != null){
                    System.out.println(declaredField + "添加了MyValue注解");
                    //取出注解的值
                    String value = myValue.value();
                    System.out.println(declaredField + "注解的值是：" + value);
                    //暴力反射
                    declaredField.setAccessible(true);

                    //判断属性类型

                    System.out.println(declaredField +"的类型为"+ declaredField.getType().getName());
                    if(declaredField.getType().getName().equals("long")){
                        Long var = Long.parseLong(value);
                        declaredField.set(student,var);
                    }else {
                        String var = value;
                        declaredField.set(student,var);
                    }
                    System.out.println(student);
                }

                System.out.println(declaredField);
            }

        }else{
            System.out.println(studentClass + "没有注解");
        }
    }
}
