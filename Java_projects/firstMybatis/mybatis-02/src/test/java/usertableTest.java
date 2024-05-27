import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import top.fixer.utils.MybatisUtils;
import top.fixer.dao.usertableDAO;
import top.fixer.pojo.usertable;
public class usertableTest {
    @Test

    public void test() {
        //测试：解决POJO属性名和字段名不一致的问题
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        usertableDAO userdao = sqlSession.getMapper(usertableDAO.class);

        usertable table = userdao.selectALL(1);

        System.out.println(table.toString());
        sqlSession.close();
    }
}
