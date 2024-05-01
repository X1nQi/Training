package com.fixer.httpObjcet;
import java.util.*;
public class HttpResponse {
    //构造方法，初始化对象
    public HttpResponse(){
        this.ResponseHeaders = new StringBuilder();
        this.ResponseBody = new StringBuilder();
        this.ResponseStatusCode = new String[1];
    }
    //-------------属性----------------
    private String uri;//请求的URI
    private String method;//响应方法
    private String HttpVersion;//响应HTTP版本
    private StringBuilder ResponseHeaders;//响应头
    private StringBuilder ResponseBody;//响应体
    private String[] ResponseStatusCode;//响应代码以及信息

    private String FileType;//响应文件类型
    //--------------属性 end-------------------


    //--------------getter start----------------------
    public String getFileType(){
        return this.FileType;
    }
    public String[] getResponseStatusCode() {
        return ResponseStatusCode;
    }
    public String getUri() {
        return uri;
    }

    public String getMethod() {
        return method;
    }

    public String getHttpVersion() {
        return HttpVersion;
    }

    public StringBuilder getResponseHeaders() {
        return ResponseHeaders;
    }


    public StringBuilder getRequestBody() {
        return ResponseBody;
    }
    //--------------getter end----------------------

    //--------------setter start-------------------
    public void setFileType(String fileType){
        this.FileType = fileType;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setHttpVersion(String httpVersion) {
        HttpVersion = httpVersion;
    }

    public void setResponseHeaders(String responseHeaders) {
        ResponseHeaders.append(responseHeaders);
        this.ResponseHeaders.append("\n");
    }

    public void setResponseBody(String responseBody) {
        this.ResponseBody.append(responseBody);
    }

    public void setResponseStatusCode(String[] code_message){
        this.ResponseStatusCode = code_message;
    }




    //--------------setter end-------------------




}
