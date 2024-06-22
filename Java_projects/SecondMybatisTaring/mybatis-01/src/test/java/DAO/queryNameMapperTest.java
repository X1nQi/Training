package DAO;

import POJO.User;
import Utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

public class queryNameMapperTest {

    @Test
    public void test(){
        //获取sqlSession
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        queryNameMapper queryNameMapper = sqlSession.getMapper(queryNameMapper.class);
        User user = queryNameMapper.queryName("张三");

        System.out.println(user.toString());

        sqlSession.close();
    }

}
