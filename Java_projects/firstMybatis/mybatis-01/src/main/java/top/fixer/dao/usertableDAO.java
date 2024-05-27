package top.fixer.dao;

import org.apache.ibatis.annotations.Select;
import top.fixer.pojo.usertable;

import java.util.List;

public interface usertableDAO {


    // 定义一个getUsertableList方法
    List<usertable> getUsertableList();
    String getUserforname(String name);

    //插入数据
    void insertUser(int pid,String name,int age);
}
