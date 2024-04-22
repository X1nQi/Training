import java.io.*;
import java.security.PublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Scanner;

public class parseBase64 {
    //序列化文件地址
    public static File SerializationFilePath =new File("D://Base64.ser");
    //序列化对象
    public static Object SerializationObject;

    public static void main(String[] args) {

    }


}

//操作序列化的类
class Base64Operation implements Serializable {
    //设置私有变量：HashMap，用于存储标签名与Base64字符串
    private HashMap<String key,String value> Base64Map 
}