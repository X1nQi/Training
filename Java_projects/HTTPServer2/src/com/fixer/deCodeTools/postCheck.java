package com.fixer.deCodeTools;

import com.fixer.globalVar.globalVar;
import com.fixer.httpObjcet.HttpRequest;
import com.fixer.httpObjcet.HttpResponse;
import com.fixer.httpServer.ParseResponse;

import java.io.File;
import java.io.FileWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.Socket;

public class postCheck implements MethodCheck{

    /*
    私有化构造方法，防止实例
     */
    private postCheck(){};
    public static void existParametersCheck(HttpResponse response) {
        System.out.println("处理了存在参数的POST请求");
    }

    public static void noParametersCheck(HttpRequest request, HttpResponse response, Socket socket) {
        System.out.println("开始处理不存在参数的POST请求");
        //判断POST请求的content-type是否为 multipart/form-data，有则为上传
        System.out.println("-->请求的内容类型为：：：" + request.getRequestHeaders().get("Content-Type"));
        //获取到请求的Content-Type类型
        String Request_contentType = request.getRequestHeaders().get("Content-Type");

        System.out.println("-->获取到的请求体：" + request.getRequestBody());
        /*

            判断是否为form-data请求
         */
        if (Request_contentType.contains("multipart/form-data")) {
            //处理上传请求
            System.out.println("处理上传请求");
            System.out.println("--》content-type：" + Request_contentType);
            //获取到 boundary
            String boundary = Request_contentType.substring(Request_contentType.indexOf("boundary=") + 9).trim();

            //=======获取到文件的类型和文件的名称
            String filename;
            String filetype;

            String regexName = "filename=\"(.+)\"";
            String regexType = "Content-Type: (.+)";

            Pattern patternName = Pattern.compile(regexName);
            Pattern patternType = Pattern.compile(regexType);

            Matcher matcherName = patternName.matcher(request.getRequestBody());
            Matcher matcherType = patternType.matcher(request.getRequestBody());

            if (matcherName.find() && matcherType.find()) {
                System.out.println("已获取到文件名和文件类型");

                filename = matcherName.group(1);
                filetype = matcherType.group(1);
                System.out.println("文件名为：" + filename + "文件类型为：" + filetype);


                //创建文件对象向服务器写入
                try {
                    String bodyStr = request.getRequestBody().trim();
                    // 使用global中的UploadURL重定向文件存储的位置
                    FileWriter fileWriter = new FileWriter(globalVar.UploadURL + filename);
                    //获取到请求体中的内容
                    /*思路：仅做单文件的上传，单文件上传时，第5行开始，直到boundary都是文件的内容
                    *先判断读取到第几行，再判断是否读到了边界
                    */
                    String[] splitBody = bodyStr.split("\r\n");
                    int flag = 0;

                    for (String i : splitBody){
                        flag++;
                        System.out.println("第"+flag+"行:"+i);
                        // 判断是否读取到了内容行
                        if(flag >= 5 && !(i.equals("--"+boundary+"--"))){
                            System.out.println("成功匹配boundary！！");
                            fileWriter.write(i+"\n");
                        }else {
                            System.out.println("不成功匹配boundary");
                        }
                    }
                    fileWriter.flush();
                    fileWriter.close();
                    System.out.println(filename+" 文件创建成功");


                    //向浏览器返回响应
                    //设置响应码
                    String[] StatusCode_M = new String[]{"200",globalVar.RESPONSE_STATUS_CODE.get("200")};
                    response.setResponseStatusCode(StatusCode_M);
                    //设置响应头, 告诉浏览器使用utf-8进行编码
                    //response.setResponseHeaders(globalVar.RESPONSE_FILE_TYPE.get(".html"));

                    response.setResponseHeaders("Content-Type: text/html;charset=utf-8");


                    //设置响应体
                    String responseBody = "<html><body><h1>文件上传成功</h1></body></html>";
                    response.setResponseBody(responseBody);
                    //发送响应
                    ParseResponse.sendResponse(response,"200",socket);
                } catch (Exception e) {
                    System.out.println("form-data 文件创建失败");
                }


            } else {// if end  else start
                System.out.println("！！！未读取到文件名和文件类型");
            }//else end

        }else {
            //________不为form-data请求---
            //处理请求体
            //处理响应体的JSON
            System.out.println("-->POST无参处理,请求体为：" + request.getRequestBody());
            //设置响应头
            response.setResponseHeaders("Content-Type: application/json;charset=utf-8");
            // 设置状态码
            String[] code_message = new String[]{"200", globalVar.RESPONSE_STATUS_CODE.get("200")};
            response.setResponseStatusCode(code_message);
            //设置响应体
            response.setResponseBody(request.getRequestBody().trim());

            ParseResponse.generateResponse(response, socket);

        }//else end


    }// method end


}// class end

