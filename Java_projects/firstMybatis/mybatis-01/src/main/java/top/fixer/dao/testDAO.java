package top.fixer.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface testDAO {

    @Select("select address from useraddress where pid =1")
    String testAddress();

    //插入
    void insertToAddress(@Param("pid") int pids, @Param("address") String addresss);
}


