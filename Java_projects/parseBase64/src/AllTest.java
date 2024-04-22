import java.util.Scanner;

public class AllTest {
    public static void main(String[] args) {
        //测试scanner读取
        Scanner scanner = new Scanner(System.in);
            System.out.print("请输入第一个字符串：");
            String s1 = scanner.next();
            //====================
            //next会去掉回车符，如果不加这一行，程序下一次读取会直接读取到被next抛弃的回车符，导致s2变量无法按照原本的意思进行读取
            //nextDouble() ，nextFloat() ，nextInt() 都会存在这个问题，需要使用nextLine()进行抛弃回车符
            scanner.nextLine();
            //====================
            System.out.print("请输入第二个字符串：");
            String s2 = scanner.nextLine();
            System.out.println("输入的字符串是："+ s1 + " " + s2);

    }
}
