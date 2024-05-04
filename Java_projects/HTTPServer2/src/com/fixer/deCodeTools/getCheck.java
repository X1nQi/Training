package com.fixer.deCodeTools;

import com.fixer.globalVar.globalVar;
import com.fixer.httpObjcet.HttpRequest;
import com.fixer.httpObjcet.HttpResponse;
import com.fixer.httpServer.ParseResponse;

import java.io.*;
import java.net.Socket;
import java.util.Base64;

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
            //--获取到文件的长度,设置到response对象中
            response.setFileLength((int)uriFile.length());
            //--获取并设置文件长度
            response.setResponseHeaders("Content-Length: "+response.getFileLength());
            //--判断并设置响应头
            response.setResponseHeaders(globalVar.judgeFileType(request.getUri()));//使用request的URI，response的uri没有设置

            //--判断是否为二进制文件
            System.out.println(response.getResponseHeaders().toString());
            if(response.getResponseHeaders().toString().contains("image")){
                response.setIsBinaryFile(true);
                System.out.println("----已判断出为图片");
            }else {
                System.out.println("----未判断出为图片");
            }

            //============wait check start ===========================
//            //存在就读取文件
//            BufferedInputStream readFile = new BufferedInputStream(new FileInputStream(uriFile));
//            //开始读取,并写入到response对象的body
//            byte[] line = new byte[response.getFileLength()];
//            int i;
//            while( (i = readFile.read(line)) != -1){
//                response.setResponseBody(new String(line,0,i));
//            }
//            //读取文件内容完毕
            //==========wait check end =======================


            //----------after diff start ------------------

            //开始读取,并写入到response对象的body   响应体
            byte[] FileDatas = new byte[response.getFileLength()];

            FileInputStream readFile = new FileInputStream(uriFile);
            readFile.read(FileDatas);
            readFile.close();

            //判断是否为图片数据
            if(response.getIsBinaryFile()){
                // 获取到图片的Base64 字符串
                String encodeImage = Base64.getEncoder().encodeToString(FileDatas);
                response.setResponseBody(encodeImage);//设置响应对象的响应体Base64字符串
            }else{//不是二进制数据，为文本数据
                response.setResponseBody(new String(FileDatas));
            }

            
            //----------after diff end-----------

            //--设置状态码
            String[] code_message = new String[]{"200",globalVar.RESPONSE_STATUS_CODE.get("200")};
            response.setResponseStatusCode(code_message);

            ParseResponse.generateResponse(response,socket);

        }else {
            //TODO:这里应该响应未找到异常
            System.out.println("uri的资源不存在！！");
            ParseResponse.sendResponse("404",socket);
        }
        System.out.println("处理了不存在参数的GET请求");
    }
}
