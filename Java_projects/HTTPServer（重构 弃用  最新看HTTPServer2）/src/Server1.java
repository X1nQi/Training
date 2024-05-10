import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server1 {
    /*
     * 抛出异常
     * */
    private  static  class DemoApplication extends RuntimeException{
        public DemoApplication() {
            super();
        }

        public DemoApplication(String message) {
            super(message);
        }

        public DemoApplication(String message, Throwable cause) {
            super(message, cause);
        }

        public DemoApplication(Throwable cause) {
            super(cause);
        }

        protected DemoApplication(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
    /*
     * 启动监听端口
     * */
    public Server1(int port) throws IOException {
        if (port < 1 || port >65535){
            throw new DemoApplication("端口错误");
        }
        ServerSocket serverSocket = new ServerSocket(port);
        ExecutorService pool = Executors.newFixedThreadPool(50);
        System.out.println("已经启动，开始监听端口"+port);
        while (true){
            Socket clientSocket = serverSocket.accept();
            if (!clientSocket.isClosed()){
                //首先服务端输出内容到客户端的输入流
                Runnable r = () -> {
                    try {
                        acceptToClient(clientSocket);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                };
                pool.submit(r);
            }
        }
    }


    private void acceptToClient(Socket clientSocket) throws IOException {
        //使用 InputStream 来读取浏览器的请求报文
        InputStream clientIn = clientSocket.getInputStream();
        //使用 BufferedReader 将InputStream强转成 BufferedReader。（InputStream读取到的是字节，BufferedReader读取到的是字符，我们需要的是字符，所以强转成BufferedReader）
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientIn,"UTF-8"));
        System.out.println("进入服务器");
        OutputStream clientOut = clientSocket.getOutputStream();
        String content = "";
        String cookieName = "";
        String cookieValue = "";
        String cookieTime = "";
        String type = "";

        /*
         * 读取第一行要进行特殊处理，从中获取到请求报文中的 方法 和 URL
         * */
        String firstLine = bufferedReader.readLine();
        System.out.println("读取到的第一行是"+firstLine);
        String requestUri = firstLine.split(" ")[1];
        System.out.println("读取数组中的URL"+requestUri);
        String method = firstLine.split(" ")[0];
        System.out.println("这次请求的方法是"+method);
        int length = 0;

        /*
         * 读取图片
         * */
        System.out.println(requestUri.split("/")[1]);
        if(requestUri.split("/")[1].equals("getPicture"))
            if(method.equals("GET")){
                System.out.println("进入update方法");
                int a = 1;
                type ="bytes";
                String path = requestUri.split("/")[3];//webapp
                System.out.println(path);
                if(!path.equals("webapp")){
                    System.out.println("访问的图片不存在");
                    clientOut.close();
                    return;
                }
                //lastIndexOf 返回此字符串中指定子字符串的最后一次出现的索引。空字符串""的最后一次出现被认为出现在索引值
                //substring 返回子字符串，子字符串从索引处开始，扩展到末尾
                String fileName = requestUri.substring(requestUri.lastIndexOf("/") + 1);//1.png
                System.out.println(fileName);
                InputStream inputStream = new FileInputStream("src/webapp/" +fileName);
                byte[] bytes = new byte[1024];
                int len = 0;
                clientOut.write("HTTP/1.1 200 OK\r\n".getBytes());//响应成功的响应头
                clientOut.write("content-type:image/jpeg\r\n".getBytes());//告诉浏览器，我这边返回的是一个图片
                clientOut.write("\r\n".getBytes());
                while ((len = inputStream.read(bytes)) != -1) {
                    clientOut.write(bytes,0,len);//图片的内容
                }
                System.out.println("执行成功");
            }

        //判断是否带路径搜索，没带路径的默认搜索ajax-demo
        String resourcePath = requestUri.equals("/") ? "ajax-demo" : requestUri.substring(1);
        System.out.println(resourcePath);
        //打印获取的html
        String a = "";
        String htmlStr = "";
        try {
            BufferedReader in = new BufferedReader(new FileReader("src/webapp/"+resourcePath +".html"));
            while ((a = in.readLine()) != null) {
                htmlStr  =htmlStr + "\n" +a;
            }
        } catch (IOException e) {
            System.out.print("错误");
        }
        //读取资源的内容
        System.out.println(htmlStr);
        //找不到资源直接返回404
        if (htmlStr == null){
            writeToClient(clientOut,"text/html",cookieName,cookieValue,cookieTime,404,"Not Found","<h1>404 FILE NOT FOUND</h1>",type,length);
            return;
        }
        content = htmlStr;
        writeToClient(clientOut,"text/html",cookieName,cookieValue,cookieTime,200,"OK",content,type,length);
    }
    /*
     * 封装响应报头（报文）
     * */
    private void  writeToClient(OutputStream clientOut, String ContentType , String cookieName, String cookieValue, String cookieTime, int responseCode, String responseDes, String content, String type, int length) throws IOException {
        clientOut.write(("HTTP/1.1 "+responseCode+ " " +responseDes+ "\r\n").getBytes());
        clientOut.write(("Date: " + (new Date()).toString() + "\r\n").getBytes());
        clientOut.write(("Content-Type: "+ ContentType +"; charset=UTF-8\r\n").getBytes());
        clientOut.write(("Set-Cookie: "+cookieName+"="+cookieValue+";"+"max-age="+cookieTime+"\r\n").getBytes());
        clientOut.write(("Accept-Ranges: "+type+"\r\n").getBytes());
        clientOut.write(("Content-Length: "+length+"\r\n").getBytes());
        clientOut.write("\r\n".getBytes());//空行
        clientOut.write(content.getBytes());
        clientOut.flush();
        clientOut.close();
    }
    public static void main(String[] args) throws IOException {
        new Server1(12345);
    }

}