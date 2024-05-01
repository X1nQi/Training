package com.fixer.httpServer;

import com.fixer.httpObjcet.HttpRequest;
import com.fixer.httpObjcet.HttpResponse;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

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
        System.out.println("生成了响应字符串");
        StringBuilder responseString = new StringBuilder();
        //填入HTTP版本
        responseString.append(response.getHttpVersion());
        responseString.append(" ");
        //填入状态码以及状态信息
        responseString.append(response.getResponseStatusCode()[0]+" "+response.getResponseStatusCode()[1]);
        responseString.append("\n");

        //填入响应头
        responseString.append(response.getResponseHeaders());
        //空行分割
        responseString.append("\r\n");
        //填入响应体
        responseString.append(response.getRequestBody());

        System.out.println("响应标头--->"+response.getResponseHeaders());
        //发送响应至客户端
        try {
            sendResponse(responseString,socket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return responseString;
    }


    //解耦，负责发送响应的函数只接受字符串和socket即可
    public static void sendResponse(StringBuilder response, Socket socket) throws IOException {
        //*发送响应字符串
        System.out.println("开始向客户端发送响应字符串");
        String allResponseDate = response.toString();
        //创建输出流
        BufferedOutputStream sentToClient = new BufferedOutputStream(socket.getOutputStream());

        //输出所有数据
        sentToClient.write(allResponseDate.getBytes());

        //关闭输出流
        sentToClient.close();
    }

}
