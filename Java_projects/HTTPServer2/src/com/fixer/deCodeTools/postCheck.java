package com.fixer.deCodeTools;

import com.fixer.httpObjcet.HttpRequest;
import com.fixer.httpObjcet.HttpResponse;

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
        System.out.println("处理了不存在参数的POST请求");
    }
}
