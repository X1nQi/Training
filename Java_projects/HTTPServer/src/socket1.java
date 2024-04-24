import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class socket1 {
    public static void main(String[] args) {
        try{
            //创建serverSocket，监听8080端口
            ServerSocket serverSocket = new ServerSocket(8080);
            for(;;){//for无限循环
                //阻塞操作，内核中pendingQueue中没有请求时会阻塞，等待有请求后拿到请求继续
                Socket socket = serverSocket.accept();//阻塞IO，等待连接，返回通讯用socket
                System.out.println("收到请求");
                //从socket中读信息
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                //创建字符缓冲流
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                //创建StringBuilder类
                StringBuilder requestBuilder = new StringBuilder();

                String line = "";
                while(!(line = bufferedReader.readLine()).isEmpty()){//如果从缓冲流中读到了空
                    requestBuilder.append(line+'\n');//将缓冲流中的信息按行读入并加入换行，保持原格式
                }
                System.out.println(requestBuilder.toString());
                //返回信息
                //设置字符缓冲流，写入，用于向客户端返回
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                //按照http协议返回信息（后边继续学习http协议）
                bufferedWriter.write("HTTP/1.1 200 OK\n\nhello world\n");
                bufferedWriter.flush();
                socket.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
