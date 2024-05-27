package top.fixer.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionException;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MybatisUtils {

    private static SqlSessionFactory sqlSessionFactory;

    // 静态块，被加载到 JVM 时就会被执行，且只执行一次
    static {
        try{
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        }catch (SqlSessionException | IOException sqle){
            sqle.printStackTrace();
        }
    }
    // 获取到 sqlSession
    public static  SqlSession getSqlSession(){
        return sqlSessionFactory.openSession();
    }

}
