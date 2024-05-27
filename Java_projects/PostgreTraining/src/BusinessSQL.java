/*
    此类用于验证事务的执行
 */

import java.sql.*;

public class BusinessSQL {

    static final String JDBC_DRIVER="org.postgresql.Driver";
    // 定义静态常量： JDBC URL \  UESR \ PASSWORD
    static final String DB_URL = "jdbc:postgresql://localhost:5432/mydb";
    static final String USER = "postgres";
    static final String PASSWD = "p1455666";

    //-----main method start --------
    public static void main(String[] args) throws ClassNotFoundException {
        Connection SqlConnection = null;
        Statement  sttm = null;

        try{
            Class.forName(JDBC_DRIVER);
            SqlConnection = DriverManager.getConnection(DB_URL,USER,PASSWD);
            //禁止自动提交
            SqlConnection.setAutoCommit(false);

            sttm =SqlConnection.createStatement();

            //创建数据表
            sttm.executeUpdate("create table jdbc_c (pid serial primary key,name varchar(20))");
            System.out.println("数据表 jdbc_c 创建成功");
            //向数据表插入内容
            sttm.executeUpdate("insert into jdbc_c (name) values ('张三')");
            System.out.println("查询数据表 jdbc_c 内容如下");
            //查询数据表
            ResultSet res = sttm.executeQuery("select * from jdbc_c");
            while (res.next()){
                System.out.println(res.getString("pid") + " "+res.getString("name"));
            }

            //清空数据表
            sttm.executeUpdate("truncate table jdbc_c");
            //查询数据表
            sttm.executeQuery("select * from jdbc_c");


            //提交事务
            SqlConnection.commit();
            //回滚
           // SqlConnection.rollback();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }


}
