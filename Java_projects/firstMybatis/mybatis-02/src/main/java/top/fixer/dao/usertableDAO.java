package top.fixer.dao;

import org.apache.ibatis.annotations.Select;
import top.fixer.pojo.usertable;

import java.util.List;

public interface usertableDAO {


    // 定义一个getUsertableList方法
    public usertable selectALL(int id);
}
