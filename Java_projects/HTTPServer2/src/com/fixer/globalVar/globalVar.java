package com.fixer.globalVar;

import java.util.HashMap;

public abstract class globalVar {
    public static String RedirectURL = "./resources";
    public static String UploadURL = "./upload/";
    public static String[] REQUEST_METHODS ={"GET","POST"}; //静态变量，存放服务器支持的请求方法
    public static HashMap<String,String> RESPONSE_STATUS_CODE;//静态变量，存放响应状态码和响应状态码对应的描述
    static {
        RESPONSE_STATUS_CODE = new HashMap<>();
        RESPONSE_STATUS_CODE.put("200","OK");
        RESPONSE_STATUS_CODE.put("404","NOT FOUND");
    }

    public static HashMap<String,String>  RESPONSE_FILE_TYPE;//静态变量，存放响应文件的类型
    static {
        RESPONSE_FILE_TYPE = new HashMap<>();
        RESPONSE_FILE_TYPE.put(".html","Content-Type: text/html");
        RESPONSE_FILE_TYPE.put(".css","Content-Type: text/css");
        RESPONSE_FILE_TYPE.put(".js","Content-Type: application/javascript");
        RESPONSE_FILE_TYPE.put(".png","Content-Type: image/png");
        RESPONSE_FILE_TYPE.put(".jpg","Content-Type: image/jpg");
        RESPONSE_FILE_TYPE.put(".jpeg","Content-Type: image/jpeg");
        RESPONSE_FILE_TYPE.put(".gif","Content-Type: image/gif");
        RESPONSE_FILE_TYPE.put(".ico","Content-Type: image/x-icon");
        RESPONSE_FILE_TYPE.put(".txt","Content-Type: text/plain");
        RESPONSE_FILE_TYPE.put(".json","Content-Type: application/json");
        RESPONSE_FILE_TYPE.put(".mp3","Content-Type: audio/mp3");

    }
    public static String judgeFileType(String filePath){
        //返回文件类型
        filePath = filePath.substring(filePath.lastIndexOf("."));
        return RESPONSE_FILE_TYPE.get(filePath);

    }
}
