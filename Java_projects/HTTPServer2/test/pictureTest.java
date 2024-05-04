import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class pictureTest {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080); // 监听8080端口

        while (true) {
            Socket socket = serverSocket.accept(); // 接受客户端连接
            new Thread(() -> {
                try {
                    // 读取图片文件
                    File file = new File("D:\\02_WORKSPACE\\GithubTraining\\Java_projects\\HTTPServer2\\pic.jpg");
                    FileInputStream fis = new FileInputStream(file);
                    byte[] data = new byte[(int) file.length()];
                    fis.read(data);
                    fis.close();

                    // 构造HTTP响应报文
                    String response = "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: image/jpg\r\n" +
                            "Content-Length: " + data.length + "\r\n" +
                            "\r\n";

                    // 发送HTTP响应报文头部
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(response.getBytes());

                    // 发送图片数据
                    outputStream.write(data);
                    outputStream.flush();

                    // 关闭连接
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}