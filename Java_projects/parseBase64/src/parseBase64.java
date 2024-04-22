import java.util.Scanner;

public class parseBase64 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMainMenu();

            int choice = scanner.nextInt();
            scanner.nextLine(); // 消耗掉输入后的换行符

            switch (choice) {
                case 1:
                    parseToBase64();
                    System.out.println("回到主菜单请输入back");
                    String backTag = scanner.nextLine();
                    if(backTag.equals("back")){
                        System.out.println("已回到主菜单");
                    }
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

    public static void parseToBase64(){
        System.out.println("请输入要进行编码的文件地址");
    }

    public static void parseTofile(){

    }
}