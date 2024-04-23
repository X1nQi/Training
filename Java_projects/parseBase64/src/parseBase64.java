import java.io.*;
import java.security.PublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Scanner;
import com.fasterxml.jackson.annotation.*;
public class parseBase64 {
    //序列化文件地址
    public static File SerializationFilePath =new File("D://Base64.ser");
    //序列化对象
    public static Object SerializationObject;

    public static void main(String[] args) {
        //接收返回的操作对象
       initSerFile(SerializationFilePath);
       Base64Operation bs64 = (Base64Operation) SerializationObject;







    }//main end

    public static void initSerFile(File serPath){
        if(serPath.exists()){
            System.out.println("ser文件已存在");
            //解序列化,解序列化的时候就完成了对静态变量的赋值
            parseBase64.SerializationObject = Base64Operation.DeserializationBase64File(SerializationFilePath);
        }else {
            try {
                serPath.createNewFile();
                System.out.println("已创建ser文件");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

//操作序列化的类
class Base64Operation implements Serializable {
    //设置私有变量：HashMap，用于存储标签名与Base64字符串
    private HashMap<String,String> Base64Map;

    //TODO:构造方法,测试初始化并赋值
    public Base64Operation(){
        Base64Map = new HashMap<>();
        System.out.println("已创建HashMap对象");
        Base64Map.put("测试标签","测试字符串");
    }

    //增加&修改HashMap
    public void add_modifyBase64Map(String key,String value){
        Base64Map.put(key,value);
        System.out.println("HashMap增加&修改方法成功");
    }

    //删除HashMap
    public void removeBase64Map(String key){
        Base64Map.remove(key);
        System.out.println("HashMap删除方法成功");
    }

    //查找HashMap
    public String findBase64Map(String key){
        String temp = Base64Map.get(key);
        System.out.println("HashMap查找方法成功");
        return temp;
    }

    //序列化方法
    public void SerializationBase64File(File filePath,Base64Operation serObj){
        System.out.println("已序列化");
        try {
            //创建文件输出流
            FileOutputStream SerializationBase64Map = new FileOutputStream(filePath);
            //置入序列流
            ObjectOutputStream SerializationObject = new ObjectOutputStream(SerializationBase64Map);
            SerializationObject.writeObject(serObj);

            SerializationBase64Map.close();
            SerializationObject.close();
            System.out.println("序列化成功");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //解序列化方法
    public static Object DeserializationBase64File(File filePath){
        try {
            //创建文件输入流
            FileInputStream SerializationBase64Map = new FileInputStream(filePath);
            //置入序列流
            ObjectInputStream SerializationObject = new ObjectInputStream(SerializationBase64Map);
            //赋值

            Object obj = SerializationObject.readObject();
            SerializationBase64Map.close();
            SerializationObject.close();

            System.out.println("解序列化成功");
            return obj;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}