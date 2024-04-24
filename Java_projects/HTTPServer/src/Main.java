import com.sun.source.tree.Scope;

import javax.imageio.spi.ImageInputStreamSpi;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {
        //服务器持续监听9090端口
        ServerSocket serverSocket = new ServerSocket(9090);
        while(true) {
            try {


                Socket httpSocket = serverSocket.accept();

                //创建缓冲流进行读取文件
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpSocket.getInputStream()));
                //创建StringBuilder类接收http请求内容
                StringBuilder httpRequestContent = new StringBuilder();
                String line = "";
                while (!((line = bufferedReader.readLine()).isEmpty())) {//判断读取到的内容是否为空
                    //将读取到的内容添加到httpRequestContent中
                    httpRequestContent.append(line + '\n');//添加换行还原原始字符串
                }
                System.out.println(httpRequestContent.toString());

                //创建Request对象，用于存放解析后的请求信息
                Request request = new Request();
                //解析请求行
                decodeRequestLine(httpRequestContent, request);
                //解析请求头
                decodeRequestHeader(httpRequestContent, request);
                //解析请求体
                decodeRequestBody(httpRequestContent, request);

                //生成响应体
                generateResponse(request, httpSocket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //解析请求头
    private static void decodeRequestHeader(StringBuilder httpRcontent,Request request) throws IOException{
        //创建一个Map对象，用于存储请求头的信息,并初始化容量
        HashMap<String,String> headers = new HashMap<>(16);
        //获取请求的原始数据，解析为字符串数组
        String[] httpContent = httpRcontent.toString().split("\n");

        //读取请求头信息，每行都是一个键值对，以空行作为请求头的标准
        String[]kv;//键值对key:values
        int tag=0;
        for(String line:httpContent){
            line = line.trim();//去除字符串两端的空格
            //判断是否为请求头信息
            tag++;
            if (tag == 1){continue;}

            if(!line.equals("")){
                kv = line.split(":");
                headers.put(kv[0],kv[1]);

            }else {//请求有请求体的情况
                //设置请求对象中的请求头信息
                request.setHeaders(headers);
                break;
            }
            //请求没有请求体的情况
            request.setHeaders(headers);
        } //for end
        System.out.println("请求头解析完成");
        System.out.println(request.getHeaders());
    }
    //解析请求行
    private static void decodeRequestLine(StringBuilder httpRcontent,Request request) throws IOException{
        //读取第一行，并分离请求方法、请求资源地址、请求HTTP版本
        //请求行示例： GET ./index.html HTTP1.1,中间使用空格分割
        String[] content = (httpRcontent.toString().split("\n"))[0].split(" ");
        request.setMethod(content[0]);//请求方法
        request.setUri(content[1]);//请求资源定位符
        request.setHttpVersion(content[2]);//请求HTTP版本
        System.out.println("请求行解析完成");
        System.out.println(request.getMethod()+" "+request.getUri()+" "+request.getHttpVersion());
    }
    //解析请求体
    private static void decodeRequestBody(StringBuilder httpRcontent,Request request) throws IOException{
        //读取到请求体，以空行作为分割
        //请求体可能为空，也可能有数据
        String[] httpContent = httpRcontent.toString().split("\n");
        StringBuilder HTTP_message = new StringBuilder();
        boolean isBody = false;
        for (String content:httpContent){
            content = content.trim();
            if (content.equals("")){
                isBody = true;
            }
            if (isBody){
                HTTP_message.append(content);
            }
        }//for end
        request.setMessage(HTTP_message.toString());
        System.out.println("请求体解析完成");
        System.out.println(request.getMessage());
    }//method end

    //生成响应体并返回给客户端
    private static void generateResponse(Request request,Socket socket) throws IOException {
         HashMap<String,String> responseHeaders = new HashMap<>(16);
         responseHeaders.put("Content-Type","text/html");
         responseHeaders.put("Location","https://www.bilibili.com/");

         Response response = new Response();
         response.setHttpVersion(request.getHttpVersion());
         response.setStatusCode("200");
         response.setStatusMessage("OK");
         response.setResponseHeaders(responseHeaders);
         response.setResponseBody("Hello HTTP Client");
         StringBuilder responseStr = new StringBuilder();
         responseStr.append(response.getHttpVersion()+" "+response.getStatusCode()+" "+response.getStatusMessage()+"\n");
         responseStr.append("Content-Type: "+response.getResponseHeaders().get("Content-Type")+"\n\n");
         responseStr.append(response.getResponseBody());
         System.out.println("\n\n响应体生成完成");
         System.out.println(responseStr.toString());
         BufferedWriter writerToClient = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
         writerToClient.write(responseStr.toString());

         writerToClient.flush(); // 确保所有数据都被写入
         socket.close();
    }
}

class Request{

    //请求的方法
    private String method;
    //请求的uri
    private String uri;

    //请求的HTTP版本
    private String httpVersion;

    //请求头
    private HashMap<String,String> headers;

    //请求体
    private String message;


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public HashMap<String, String> getHeaders() {
        return this.headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

class Response{
    private String httpVersion;//响应的http版本

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    private String statusCode;//响应的状态码
    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    private String statusMessage;//响应的状态消息
    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    private HashMap<String,String> ResponseHeaders;//响应头
    public HashMap<String, String> getResponseHeaders() {
        return ResponseHeaders;
    }

    public void setResponseHeaders(HashMap<String, String> responseHeaders) {
        this.ResponseHeaders = responseHeaders;
    }

    private String ResponseBody;//响应体
    public String getResponseBody() {
        return ResponseBody;
    }

    public void setResponseBody(String responseBody) {
        this.ResponseBody = responseBody;
    }



}