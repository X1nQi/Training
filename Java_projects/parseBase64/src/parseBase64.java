import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Base64;
import java.util.HashMap;
import java.util.Scanner;

public class parseBase64 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        printMainMenu();
        //实例化Base64About对象
        Base64About base64About = new Base64About();

        boolean running = true;

        while (running) {
            int choice = scanner.nextInt();
            scanner.nextLine(); // 消耗掉输入后的换行符

            switch (choice) {
                case 1:
                    base64About.codingToBase64(scanner);
                    break;
                case 2:
                    System.out.println("请输入要进行解码的字符串或文本文件地址");

                    break;
                case 3:
                    System.out.println("程序已退出");
                    running = false;
                    break;
                default:
                    System.out.println("无效的选项，请重新输入。");
            }
        }

        scanner.close();
    }//main end
    public static void printMainMenu(){
        System.out.println("请选择操作：");
        System.out.println("1. 编码Base64");
        System.out.println("2. 解码Base64");
        System.out.println("3. 退出程序");
        System.out.print("输入选项（1-3）：");
    }

    //TODO:启动时序列化文件
    public static void initFile(){
        File storageFile = new File("./Base64Storage.ser");
        if(storageFile.exists()){//文件存在
            //TODO:反序列化文件
        }else{//不存在
            try {//创建文件
                storageFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    //TODO:执行序列化
    public static void serializeFile(){

    }
    //TODO:执行反序列化
    public static void deserializeFile(){

    }
    //创建Base64相关类
    public static class Base64About{
        //进行编码
        public void codingToBase64(Scanner scn){
            boolean running = true;
            while(running){
            System.out.print("请输入要进行编码的文件地址:");
            String filePath = scn.nextLine();
            String Base64Str;

            File fileobj = new File(filePath);
            if (fileobj.exists() && fileobj.isFile()){
                //对文件进行编码返回给byte数组
                byte[] encodeBytes = Base64.getEncoder().encode(filePath.getBytes());
                Base64Str = new String(encodeBytes);

                System.out.println("->输入back回到主菜单，输入其他任意字符继续进行解码<-");
                String backTag = scn.nextLine();
                    if (backTag.equals("back")){
                        running = false;
                        printMainMenu();
                    }//if end
                }else{
                    System.out.println("->输入的路径不存在或者路径不是文件，请重新输入路径<-");
                }//if end
            }//while end
        }// method end

        //进行解码
        public void parseTofile(){

        }
    }
}// parseBase64 end


//用来存储状态的对象
class StorageEnCode implements Serializable {
    private HashMap<String,String> base64Map;

    //1.获取所有base64Map值
    public HashMap<String, String> getAllbase64Map(){
        return base64Map;
    }
    //2.获取制定base64Map的值
    public String getBase64Map(String key){
        return base64Map.get(key);
    }
    //3.增加&修改 base64Map
    public void addBase64Map(String key,String value){
        base64Map.put(key,value);
    }
    //4.指定删除base64Map中的一项
    public void removebase64Map(String key){
        base64Map.remove(key);
    }

}