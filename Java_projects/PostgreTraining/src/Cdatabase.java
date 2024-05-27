
import java.sql.*;
public class Cdatabase {
    /*
     *  此类用于验证创建数据库的语句的可行性
     *  验证结果是：可以通过JDBC创建数据库
     */
    static final String JDBC_DRIVER="org.postgresql.Driver";
    // 定义静态常量： JDBC URL \  UESR \ PASSWORD
    static final String DB_URL = "jdbc:postgresql://localhost:5432/mydb";
    static final String USER = "postgres";
    static final String PASSWD = "p1455666";
    public static void main(String[] args) {
        Connection coon = null;
        Statement sttm = null;


        try {
            Class.forName(JDBC_DRIVER);

            coon = DriverManager.getConnection(DB_URL,USER,PASSWD);
            sttm = coon.createStatement();

            sttm.executeUpdate("CREATE DATABASE jdbc_database");
        }catch (SQLException e){
            e.printStackTrace();

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
