package top.fixer.pojo;

public class usertable {

    // 变量名要和数据库内的保持一致
    private int pid;
    private String username;
    private int age;

    // 有参构造方法
    public usertable(int pid, String name, int age) {
        this.pid = pid;
        this.username = name;
        this.age = age;
        System.out.println("有参构造方法");
    }

    @Override
    public String toString() {
        return "usertable{" +
                "pid=" + pid +
                ", name='" + username + '\'' +
                ", age=" + age +
                '}';
    }

    // 无参构造方法
    public usertable() {
    }

    public int getPid() {
        return pid;
    }

    public String getUsername() {
        return username;
    }

    public int getAge() {
        return age;
    }
}
