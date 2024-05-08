package com.fixer.httpServer;

import com.fixer.httpObjcet.HttpRequest;
import com.fixer.httpObjcet.HttpResponse;
import com.fixer.globalVar.globalVar;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ParseResponse {

    /*
    这个类是用来生成响应字符串，并发送的
     */

    private ParseResponse(){};//私有化，禁止进行实例化


    /*装载response对象
    * 设置响应对象的方法
    * 设置响应对象的HTTP版本
    * 设置响应对象的响应状态码
     */
    public static void LoadResponse(HttpRequest request,HttpResponse response){
        response.setMethod(request.getMethod());
        response.setHttpVersion(response.getHttpVersion());

    }

    public static StringBuilder generateResponse(HttpResponse response,Socket socket){
        //*生成响应字符串
        System.out.println("-->生成了响应字符串");
        StringBuilder responseString = new StringBuilder();
        //填入HTTP版本
        responseString.append(response.getHttpVersion());
        responseString.append(" ");
        //填入状态码以及状态信息
        responseString.append(response.getResponseStatusCode()[0]+" "+response.getResponseStatusCode()[1]);
        responseString.append("\r\n");

        //填入响应头
        responseString.append(response.getResponseHeaders());
        //空行分割
        responseString.append("\r\n");


        //填入响应体
        //responseString.append(response.getRequestBody());

        System.out.println("响应标头--->"+response.getResponseHeaders());
        //发送响应至客户端
        try {
            sendResponse(responseString,socket,response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return responseString;
    }


    //解耦，负责发送响应的函数只接受字符串和socket即可
    public static void sendResponse(StringBuilder response, Socket socket,HttpResponse responseObj) throws IOException {
        //*发送响应字符串
        System.out.println("开始向客户端发送响应字符串");
        String allResponseDate = response.toString();


        BufferedOutputStream sentToClient = new BufferedOutputStream(socket.getOutputStream());
        //输出所有数据
        sentToClient.write(allResponseDate.getBytes());
        //判断是否为二进制数据
        if(responseObj.getIsBinaryFile()){
            //DONE:getRequestBody 的返回类型需要改为String
            byte[] decodeBytes = Base64.getDecoder().decode(responseObj.getRequestBody());
            System.out.println("分析出二进制数据，进行解码后："+decodeBytes);
            sentToClient.write(decodeBytes);
        }else {
            sentToClient.write(responseObj.getRequestBody().getBytes());
            System.out.println("为非二进制数据");
            System.out.println("响应体为："+responseObj.getRequestBody());
        }
        sentToClient.flush();

        //关闭socket连接
        System.out.println("》》关闭socket连接《《");
        //关闭输出流
        sentToClient.close();
        socket.close();
    }
    public static void sendResponse(HttpResponse response,String Code,Socket socket) throws IOException {
        //METHOD: 重构方法，用于不生成响应，直接返回响应的情况
        //TODO：写死的HTTP版本，日后想着怎么改吧
        String responseLine = "HTTP/1.1 "+Code+" "+globalVar.RESPONSE_STATUS_CODE.get(Code)+"\r\n";
        String responseHead = response.getResponseHeaders()+"\n".toString();
        String responseBody = response.getRequestBody().toString();

        BufferedOutputStream sentToClient = new BufferedOutputStream(socket.getOutputStream());
        sentToClient.write(responseLine.getBytes());
        sentToClient.write(responseHead.getBytes());
        sentToClient.write(responseBody.getBytes(StandardCharsets.UTF_8));

        sentToClient.flush();
        sentToClient.close();
        socket.close();
    }


}// Class end
