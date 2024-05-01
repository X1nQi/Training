package com.fixer.httpServer;

import com.fixer.deCodeTools.getCheck;
import com.fixer.deCodeTools.postCheck;
import com.fixer.httpObjcet.HttpRequest;
import com.fixer.httpObjcet.HttpResponse;

import java.io.*;
import java.net.Socket;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.HashMap;

import static com.fixer.globalVar.globalVar.*;

public class ParseRequest {

/*
    这个类的目的就是处理Request对象，这个类的构造函数运行完，将得到HTTP请求的所有结构
    1.请求方法
    2.请求URL
    3.请求HTTP版本
    4.请求头
    5.请求体
    所以这个类的构造方法接受一个HttpRequest对象作为参数，并在构造方法中完成对HttpRequest对象的解析
 */
    public ParseRequest(HttpRequest request, Socket socket) throws IOException {
        /*

        构造方法
        1.获取请求
        2.解析请求
                 */
        //创建响应对象
        HttpResponse response = new HttpResponse();
//        获取输入流
        BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String line;
        while((line = inputStream.readLine()) != null){
            if(line.equals("")){
                System.out.println("已读取全部请求");
                break;
            }else {
                request.appendAllRequestStr(line);
                request.appendAllRequestStr("\n");
            }
        }

        //----解析请求 start----------
        decodeRequestLine(request);
        decodeRequestHeader(request);
        decodeRequestBody(request);
        //----解析请求 end----------

        /*
            判断方法是否存在
         */
        //将方法数组转换为列表，并利用contains方法判断是否存在请求的方法
        if(Arrays.asList(REQUEST_METHODS).contains(request.getMethod().toUpperCase())){
            System.out.println("请求方法合法");
            //将请求的方法设置到响应对象的属性中
            response.setMethod(request.getMethod().toUpperCase());
            //将请求的HTTP版本设置到响应对象的属性中
            response.setHttpVersion(request.getHttpVersion());

        }else {
            //TODO:这里应该响应异常
            System.out.println("返回方法非法响应！");
        };

        /*
        2.判断是否有参数传递
         */
        if(request.getRequestParameters() != null){
            //有参数
            System.out.println("有参数传递");
            switch (request.getMethod().toUpperCase()){
                case "GET" -> getCheck.existParametersCheck(response);
                case  "POST" -> postCheck.existParametersCheck(response);
                default -> {return;}
            }

        }else {
            //无参数
            System.out.println("无参数传递");
            switch (request.getMethod().toUpperCase()){
                case "GET" -> getCheck.noParametersCheck(request,response,socket);
                case  "POST" -> postCheck.noParametersCheck(request,response,socket);
                default -> {return;}
            }
        }
    }


    // 处理请求行
    private void decodeRequestLine(HttpRequest request){
        System.out.println("处理请求行开始");
        //*分离第一行，取出请求方法  请求路径  请求HTTP版本号
        String requestLine = (request.getAllRequestStr().split("\n"))[0];
        String[] requestLineArr = requestLine.split(" ");
        request.setMethod(requestLineArr[0]);
        request.setUri(requestLineArr[1]);
        request.setHttpVersion(requestLineArr[2]);

        //DONE:处理请求参数
        if(request.getUri().contains("?")){
            //取出uri和参数
            String[] uri_parameter = request.getUri().split("\\?");
            //设置uri
            request.setUri(uri_parameter[0]);

            HashMap<String,String> kv =new HashMap<>(16);
            //分离参数列表
            String[] parameterArr = uri_parameter[1].split("&");
            for(String parameter : parameterArr){
                //分离键值
                String[] kvArr = parameter.split("=");
                //插入键值
                kv.put(kvArr[0],kvArr[1]);
            }

            //设置request对象
            request.setRequestParameters(kv);
            System.out.println("处理请求行结束");
            System.out.println(kv);

        }
        //DONE:处理资源路径
        String uri = request.getUri();
        if(uri.equals("/") || uri.equals("/index.html") || uri.equals("/index")){
            request.setUri("./index.html");
            System.out.println("资源路径为"+request.getUri());
        }else {
            request.setUri("."+uri);
        }
    }

    //处理请求头
    private void decodeRequestHeader(HttpRequest request){

        System.out.println("处理请求头开始");

        // 通过字符串数组获取分割后的字符串
        String[] allRequestStrArr = request.getAllRequestStr().split("\n");
        HashMap<String,String> kv =new HashMap<>(16);
        // 分离第一行，即请求行，一直读取到空行
        for(int i = 0;i<allRequestStrArr.length;i++){
            if(i == 0){continue;};
            //TODO:这地方能不能判断出请求头和请求体中的空行？有待测试
            if(allRequestStrArr[i].equals("")){break;};
            //向键值对添加东西
             kv.put(allRequestStrArr[i].split(":")[0],allRequestStrArr[i].split(":")[1]);
        }
        //设置到request对象中
        request.setRequestHeaders(kv);
        System.out.println(request.getRequestHeaders());
    }

    //处理请求体
    private void decodeRequestBody(HttpRequest request){
        System.out.println("处理请求体开始");

        //获取读取到的全部字符串
        String requestAllStr = request.getAllRequestStr();

        String[] allRequestStrArr = requestAllStr.split("\n");
        StringBuilder requestBody = new StringBuilder();
        boolean isBody = false;
        for(String temp : allRequestStrArr){
            //去空
            //temp =temp.trim();

            if(isBody){
                requestBody.append(temp);
            }


            //如果temp等于换行
            if(temp.equals("\n")){//TODO:这地方能不能判断出请求头和请求体中的空行？有待测试
                isBody = !isBody;
            }
        }
        request.setRequestBody(requestBody.toString());
        System.out.println("处理请求体开始");
        System.out.println("请求体为"+(requestBody.toString()));
    }



}
