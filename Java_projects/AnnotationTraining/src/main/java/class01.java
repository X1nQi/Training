import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


public class class01 {
    @MyAnnotation( name = "李四")
    public void test1(){

    }

    @MyAnnotation2("测试")
    public void test2(){
        
    }
}


@Retention(RetentionPolicy.RUNTIME)
@Target( value = {ElementType.METHOD,ElementType.TYPE})
@interface MyAnnotation{
    String name() default "张三";
}

@Retention(RetentionPolicy.RUNTIME)
@Target( value = {ElementType.METHOD,ElementType.TYPE})
@interface MyAnnotation2{
    String value();
}


