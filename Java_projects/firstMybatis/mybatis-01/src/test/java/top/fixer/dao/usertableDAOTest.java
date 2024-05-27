package top.fixer.dao;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import top.fixer.pojo.usertable;
import top.fixer.utils.MybatisUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class usertableDAOTest {
    @Test
    public void test(){
        // 获取session 对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        //获取usertabledao 对象
        usertableDAO mapper = sqlSession.getMapper(usertableDAO.class);
//      //获取 useraddressdao 对象
        uesraddressDAO addressMapper = sqlSession.getMapper(uesraddressDAO.class);
        //获取 testDAO
        testDAO testMapper = sqlSession.getMapper(testDAO.class);

        //使用 testDAO 的testAddress 方法
        String test = testMapper.testAddress();
        System.out.println("测试："+test);
        //使用 testDAO 的insertToAddress方法
        testMapper.insertToAddress(5,"测试地址2");

        //调用查询方法
        List<usertable> usertableDAOList = mapper.getUsertableList();

        //调用查询方法
        String address  = addressMapper.getuserAddress();
        System.out.println("地址为："+address);

        for(usertable usertable : usertableDAOList ){
            System.out.println("数据: "+usertable.getPid()+"  "+usertable.getName()+"  "+usertable.getAge());
        }

        String q_name = mapper.getUserforname("张三");
        System.out.println(q_name);

        usertable test_user = new usertable(4,"测试用户",20);

        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void testMap(){
        //打开数据库会话链接
        SqlSession sqlSession = MybatisUtils.getSqlSession();


        anyParametersDAO andao =  sqlSession.getMapper(anyParametersDAO.class);
        HashMap<String, Object> u_map = new HashMap<>();
        u_map.put("u_id",6);
        u_map.put("u_address","测试用户6");
        andao.insetToads(u_map);

        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void testSelect(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        anyParametersDAO an = sqlSession.getMapper(anyParametersDAO.class);

        String name = an.getUserforname();

        System.out.println(name);
        sqlSession.close();
    }

    //测试 typeAliases
    @Test
    public void testAlias(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        usertableDAO usertable = sqlSession.getMapper(usertableDAO.class);

        String name = usertable.getUserforname("张三");
        System.out.println(name);
        System.out.println("测试成功");
        sqlSession.close();
    }
}
