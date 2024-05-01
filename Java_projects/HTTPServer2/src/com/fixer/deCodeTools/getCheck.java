package com.fixer.deCodeTools;

import com.fixer.globalVar.globalVar;
import com.fixer.httpObjcet.HttpRequest;
import com.fixer.httpObjcet.HttpResponse;
import com.fixer.httpServer.ParseResponse;

import java.io.*;
import java.net.Socket;

public class getCheck implements MethodCheck{

    /*
    私有化构造方法，防止实例化
     */
    private getCheck(){};
    public static void existParametersCheck(HttpResponse response) {
        System.out.println("处理了存在参数的GET请求");

    }

    public static void noParametersCheck(HttpRequest request, HttpResponse response, Socket socket) throws IOException {
        //无参数调用方法
        // 首先判断请求的Uri是否存在并且是否为文件
        File uriFile = new File(request.getUri());
        if(uriFile.exists() && uriFile.isFile()){
            System.out.println("uri的资源存在");
            //存在就读取文件
            BufferedInputStream readFile = new BufferedInputStream(new FileInputStream(uriFile));
            //开始读取,并写入到response对象的body
            byte[] line = new byte[8192];
            int i;
            while( (i = readFile.read(line)) != -1){
                response.setResponseBody(new String(line,0,i));
            }
            //读取文件内容完毕


            String[] code_message = new String[]{"200",globalVar.RESPONSE_STATUS_CODE.get("200")};
            //设置状态码
            response.setResponseStatusCode(code_message);
            //设置响应头
            response.setResponseHeaders(globalVar.judgeFileType(request.getUri()));//使用request的URI，response的uri没有设置
            ParseResponse.generateResponse(response,socket);

        }else {
            //TODO:这里应该响应未找到异常
            System.out.println("uri的资源不存在！！");
        }
        System.out.println("处理了不存在参数的GET请求");
    }
}
