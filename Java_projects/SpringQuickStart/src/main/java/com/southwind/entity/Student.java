package com.southwind.entity;


import com.southwind.test.MyComponent;
import com.southwind.test.MyValue;
import lombok.Data;

@Data
@MyComponent
public class Student {
//    使用lombok简化了实体类的开发
    @MyValue("22")
    private long id;
    @MyValue("张三")
    private String name;
    private  int age;
}
