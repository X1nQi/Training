

import java.sql.*;
//TIP 要<b>运行</b>代码，请按 <shortcut actionId="Run"/> 或
// 点击装订区域中的 <icon src="AllIcons.Actions.Execute"/> 图标。
public class Main {


    /*
        1.创建数据库
        2.创建数据表
        3.填充数据
        4.查询数据
        5.删除指定数据
        6
     */


    static final String JDBC_DRIVER="org.postgresql.Driver";
    // 定义静态常量： JDBC URL \  UESR \ PASSWORD
    static final String DB_URL = "jdbc:postgresql://localhost:5432/mydb";
    static final String USER = "postgres";
    static final String PASSWD = "p1455666";

    //========test sql===========
    static final String insertTest = "insert into user_table (name) values ('夏洛蒂')";
    static final String queryAllTest = "select * from user_table";

    static String[] nameList= new String[]{"沈勇", "张天赫", "宋子辰", "乔天昊", "胡一鸣", "吴泽远", "康强", "任娟", "贾雨涵", "丁宜豪"};

    //    test end
    public static void main(String[] args) throws ClassNotFoundException {
        //载入驱动
        Class.forName(JDBC_DRIVER);
        //创建Connection 对象与 Statement 对象
        Connection conn  = null;
        Statement sttm  = null;

        try{
            //获取数据库连接对象
            conn = DriverManager.getConnection(DB_URL, USER, PASSWD);
            //获取SQL操作对象
            sttm = conn.createStatement();
            //插入数据
            for(String temp : nameList){
                StringBuilder tempStr = new StringBuilder();
                tempStr.append("insert into user_table (name) values ('");
                tempStr.append(temp);
                tempStr.append("')");
                System.out.println(tempStr.toString());
                sttm.executeLargeUpdate(tempStr.toString());
            }

            System.out.println("insert data successful!");

            //查询表
            ResultSet reSet = sttm.executeQuery(queryAllTest);
            System.out.println("-----查询 user_table 表所有数据如下------");
            while(reSet.next()){
                System.out.println("   用户ID:"+reSet.getString("id")+"    用户名:"+reSet.getString("name"));
            }

            //删除表的内容
            sttm.executeUpdate("truncate table user_table");
            System.out.println("已清空表的内容");


            sttm.close();
            conn.close();

        }catch (SQLException sqlerr){

            sqlerr.printStackTrace();
        }

    }


}