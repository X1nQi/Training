package POJO;

public class User {
    int user_id;
    String user_name;
    int user_age;

    public int getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public int getUser_age() {
        return user_age;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", user_name='" + user_name + '\'' +
                ", user_age=" + user_age +
                '}';
    }
}
