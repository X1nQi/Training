import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;

public class Main {
    //定义请求方法
    private static final int PORT = 9090;
    private static final String[] METHODS = {"GET","POST","PUT","DELETE"};
    //定义状态码以及状态说明
    private static final HashMap<String,String> STATUSCODE_AND_MESSAGE = new HashMap<String,String>(){{
        put("200","OK");
        put("404","Not Found");
        put("500","Internal Server Error");
        put("301","Found");//临时重定向
        put("501","Not Implemented");
    }};

    public static void main(String[] args) throws IOException {
        //服务器持续监听9090 端口
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("服务器已启动");
        while(true) {
            try {
                //返回通信用的Socket
                Socket httpSocket = serverSocket.accept();

                //处理HTTP请求，返回request对象
                Request request = handleRequest(httpSocket);
                //返回响应体
                returnResponse(generateResponse(request),httpSocket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 处理HTTP请求 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    // 1. 处理HTTP请求入口函数
    private static Request handleRequest(Socket httpSocket) throws IOException {
        //创建字符缓冲流进行读取文件
        //!!因为接受HTTP请求时，没有什么二进制数据需要处理，所以使用字符流更方便我处理请求数据
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpSocket.getInputStream()));
        //创建StringBuilder类接收http请求内容
        StringBuilder httpRequestContentStr = new StringBuilder();

        String line = "";


        while ((line = bufferedReader.readLine()) != null) {//当能读取到数据时循环读取，直到读不到
            //将读取到的内容添加到httpRequestContent中
            if(line.equals("")) {
                break;
            }
            httpRequestContentStr.append(line + '\n');//添加换行还原原始请求字符串
        }

        //--test print
        System.out.println("获取到的HTTP请求为："+httpRequestContentStr.toString());

        //创建Request对象，用于存放解析后的请求信息
        Request request = new Request();
        //解析请求行
        decodeRequestLine(httpRequestContentStr, request);
        //解析请求头
        decodeRequestHeader(httpRequestContentStr, request);
        //解析请求体
        decodeRequestBody(httpRequestContentStr, request);

        //返回请求体对象
        return request;
    }

    // 2. 解析请求头
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

    //3. 解析请求行
    private static void decodeRequestLine(StringBuilder httpRcontent,Request request) throws IOException{
        //读取第一行，并分离请求方法、请求资源地址、请求HTTP版本
        //请求行示例： GET /index.html HTTP1.1,中间使用空格分割
        String[] content = (httpRcontent.toString().split("\n"))[0].split(" ");
        System.out.println(Arrays.toString(content));
        System.out.println("114行-解析出请求方法："+content[0]);
        request.setMethod(content[0]);//请求方法
        request.setUri(content[1]);//请求资源定位符
        request.setHttpVersion(content[2]);//请求HTTP版本

        //--test print
        System.out.println("请求行解析完成");
        System.out.println(request.getMethod()+" "+request.getUri()+" "+request.getHttpVersion());
    }

    //4. 解析请求体
    private static void decodeRequestBody(StringBuilder httpRcontent,Request request) throws IOException{
        //读取到请求体，以换行作为分割
        //请求体可能为空，也可能有数据
        String[] httpContent = httpRcontent.toString().split("\n");
        StringBuilder HTTP_message = new StringBuilder();
        boolean isBody = false;

        //找到请求体部分
        for (String content:httpContent){
            //去除字符串两端的空格
            content = content.trim();
            if (content.isEmpty()){//TODO:这里的 .equals替换为了 .isEmpty，不知道会不会有什么问题，现在先用着，后面再测试
                //如果读取到空行，说明已经读取到请求体部分，将isBody设置为true，下面的读取到的内容都要写入请求体内
                isBody = true;
            }

            if (isBody){
                HTTP_message.append(content);
            }
        }//for end
        //设置请求体
        request.setMessage(HTTP_message.toString());
        //--test print
        System.out.println("请求体解析完成");
        System.out.println(request.getMessage());
    }//method end

    //↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 处理HTTP请求 end ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑


    //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 处理HTTP响应 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    //1. 返回响应入口函数
    private static void returnResponse(StringBuilder responseStr,Socket socket) throws IOException {//参数：

        BufferedOutputStream WriterStreamToClient = new BufferedOutputStream(socket.getOutputStream());
        WriterStreamToClient.write(responseStr.toString().getBytes());
        WriterStreamToClient.flush();
        System.out.println("Sock为："+socket);
        socket.close();

        System.out.println("已向客户端返回响应,已关闭Socket连接");
    }


    //2. 生成响应体
    private static StringBuilder generateResponse(Request request) throws IOException {
        StringBuilder responseStr = new StringBuilder();
        //处理响应，首先会校验请求行的方法和资源，不通过就再处理响应头和响应体
        if(!handleResponseLine(request,responseStr)){
            //处理响应头
            handleResponseHeader(request,responseStr);
            //处理响应体
            handleResponseBody(request,responseStr);
        }

        return responseStr;

    }

    //3. 处理响应行
    private static boolean handleResponseLine(Request request,StringBuilder responseAll){
        //处理请求行是在处理什么？
        //1. 处理请求方法
        //2. 处理请求资源地址
        //返回值判断请求行是否出错，true为出错，false为未出错
        boolean isExistMethod = false;//返回请求方法是否存在
        boolean isExistFile = false;//返回资源文件是否存在
        String filePath = "";//重定向资源路径
        File requestFile = null;//作为请求文件的File对象

        //1. 循环方法数组，判断请求方法是否被服务器支持
        for(String method:METHODS){
            if(method.equals(request.getMethod())){
                //服务器支持就把 isExistMethod设置为 true
                isExistMethod = true;
            }
        }
        filePath = routerTransform(request.getUri());//转换路由
        //TODO:这里是个坑，是为了下面设置响应头做准备，不应该这么写
        request.setUri(filePath);
        //判断请求的资源是否存在
        File httpRequestFile = new File(filePath);
        //uri存在，并且是文件的情况下，isExistFile设置为true
        if (httpRequestFile.exists() && httpRequestFile.isFile()){
            isExistFile = true;
        }

        if(isExistMethod&&isExistFile){//在方法列表中找到请求方法，并且请求的资源存在
            //添加HTTP版本和状态码&信息
            responseAll.append(request.getHttpVersion()+" ");
            responseAll.append("200 "+STATUSCODE_AND_MESSAGE.get("200")+"\n");
            //--test print
            System.out.println("对请求方式和请求资源做出判断：true");
            return false;//没有错误信息
        }else {
            //找不到资源，返回404
            responseAll.append(request.getHttpVersion()+" ");
            responseAll.append("404 "+STATUSCODE_AND_MESSAGE.get("404")+"\n");
            return true;//有错误信息
        }
    }//method end

    //4. 处理响应头
    private static void handleResponseHeader(Request request,StringBuilder responseAll){
        //设置响应头属性
        String resourceNmae = request.getUri();//资源文件名
        //获取.最后出现的索引，并从这个索引取到最后，获取完整的文件类型名
        System.out.println("handleResponseHeader方法内获取到的资源文件名："+resourceNmae);
        String Types = resourceNmae.substring(resourceNmae.lastIndexOf("."));
        System.out.println("handleResponseHeader方法内获取到的文件后缀："+Types);
        File requestFile = new File(routerTransform(request.getUri()));//创建请求文件的File对象
        String FileSize = String.valueOf(requestFile.length());//获取文件大小
        //匹配请求文件类型，并返回Content-Type的值
        switch (Types) {
            case ".html" -> responseAll.append("Content-Type:text/html;charset=UTF-8\n")
                    .append("Content-Length:"+FileSize+"\n").append("Connection:close\n");
            case ".css" -> responseAll.append("Content-Type:text/css;charset=UTF-8\n")
                    .append("Content-Length:"+FileSize+"\n").append("Connection:close\n");
            case ".js" -> responseAll.append("Content-Type:text/javascript;charset=UTF-8\n")
                    .append("Content-Length:"+FileSize+"\n").append("Connection:close\n");
            case ".png" -> responseAll.append("Content-Type:image/png\n")
                    .append("Content-Length:"+FileSize+"\n").append("Connection:close\n");
            case ".jpg" -> responseAll.append("Content-Type:image/jpeg\n")
                    .append("Content-Length:"+FileSize+"\n").append("Connection:close\n");
            case ".ico" -> responseAll.append("Content-Type:image/x-icon\n")
                    .append("Content-Length:"+FileSize+"\n").append("Connection:close\n");
            case ".json" -> responseAll.append("Content-Type:application/json;charset=UTF-8\n")
                    .append("Content-Length:"+FileSize+"\n").append("Connection:close\n");
            case ".txt" -> responseAll.append("Content-Type:text/plain;charset=UTF-8\n")
                    .append("Content-Length:"+FileSize+"\n").append("Connection:close\n");
            case ".xml" -> responseAll.append("Content-Type:text/xml;charset=UTF-8\n")
                    .append("Content-Length:"+FileSize+"\n").append("Connection:close\n");
            case ".pdf" -> responseAll.append("Content-Type:application/pdf;charset=UTF-8\n")
                    .append("Content-Length:"+FileSize+"\n").append("Connection:close\n");
            case ".zip" -> responseAll.append("Content-Type:application/zip\n")
                    .append("Content-Length:"+FileSize+"\n").append("Connection:close\n");
            default -> responseAll.append("Content-Type:application/octet-stream\n")
                    .append("Content-Length:"+FileSize+"\n").append("Connection:close\n");
        }


    }

    //5. 处理响应体
    private static void handleResponseBody(Request request,StringBuilder responseAll){
        //读取文件的内容，使用字节缓冲流
        try {
            responseAll.append("\n");
            //使用字节流进行读取，因为这是文件
            FileInputStream requestFile = new FileInputStream(routerTransform(request.getUri()));
            BufferedInputStream buff_requestFile = new BufferedInputStream(requestFile);

            int len;
            byte[] FileConten = new byte[4096];
            //读取文件的所有字节
            while ((len = buff_requestFile.read(FileConten)) != -1){
                responseAll.append(new String(FileConten,0,len));
            }
            //--test print
            System.out.println("已处理完响应体，将文件字节全部写入暂存字符串");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //路由处理，处理请求中的资源

    //↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 处理HTTP响应 end ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    //========================Tools======================

    // 1. 路由转换，传入url，返回对于服务器的uri
    private static String routerTransform(String uri){
        System.out.println("转化前的uri:"+uri);
        if(uri.equals("/") || uri.equals("/index") || uri.equals("/index.html")){
            return "./index.html";
        }else{
            return "./" +uri;
        }
    }
    //========================Tools end =================



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