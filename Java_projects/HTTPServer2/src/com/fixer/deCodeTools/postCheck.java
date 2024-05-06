package com.fixer.deCodeTools;

import com.fixer.globalVar.globalVar;
import com.fixer.httpObjcet.HttpRequest;
import com.fixer.httpObjcet.HttpResponse;
import com.fixer.httpServer.ParseResponse;

import java.net.Socket;

public class postCheck implements MethodCheck{

    /*
    私有化构造方法，防止实例化
     */
    private postCheck(){};
    public static void existParametersCheck(HttpResponse response) {
        System.out.println("处理了存在参数的POST请求");
    }

    public static void noParametersCheck(HttpRequest request, HttpResponse response, Socket socket) {
        System.out.println("开始处理不存在参数的POST请求");
        //判断POST请求的content-type是否为 multipart/form-data，有则为上传
        System.out.println("-->请求的内容类型为：：："+request.getRequestHeaders().get("Content-Type"));
        //获取到请求的Content-Type类型
        String Request_contentType = request.getRequestHeaders().get("Content-Type");
        if(Request_contentType.contains("multipart/form-data")){
            //处理上传请求
            System.out.println("处理上传请求");
        }




        //处理请求体
        //处理响应体的JSON
        System.out.println("-->POST无参处理,请求体为："+request.getRequestBody());
        //设置响应头
        response.setResponseHeaders("Content-Type: application/json;charset=utf-8");
        // 设置状态码
        String[] code_message = new String[]{"200", globalVar.RESPONSE_STATUS_CODE.get("200")};
        response.setResponseStatusCode(code_message);
        //设置响应体
        response.setResponseBody(request.getRequestBody().trim());

        ParseResponse.generateResponse(response,socket);

    }
}
