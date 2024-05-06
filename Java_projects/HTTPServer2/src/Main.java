import com.fixer.httpObjcet.HttpRequest;
import com.fixer.httpObjcet.HttpResponse;
import com.fixer.httpServer.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;
import java.util.HashMap;

public class Main {


    public static void main(String[] args) throws IOException {
        //创建HttpRequest对象 和 HttpResponse对象
        ServerSocket serverSocket = new ServerSocket(9090);

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("已与服务器建立连接");
            //创建ParseRequest对象，用于处理请求，构造函数会完成请求行，请求头，请求体的解析
            new newThread(socket).start();

        }

    }
}