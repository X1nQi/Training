package top.fixer.dao;

import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface anyParametersDAO {

    public void insetToads(Map<String, Object> t_map);
    @Select("select name from usertable where pid= 1")
    String getUserforname();
}


