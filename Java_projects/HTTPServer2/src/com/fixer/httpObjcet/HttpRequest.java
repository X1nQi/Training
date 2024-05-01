package com.fixer.httpObjcet;
import java.util.*;

public class HttpRequest {
    public HttpRequest(){
        allRequestStr = new StringBuilder();
    }
    //---------------属性---------------------------
    private String uri;//请求URI
    private String method;//请求方法

    private String HttpVersion;//请求HTTP版本
    private HashMap<String,String> RequestHeaders;//请求头
    private String RequestBody;//请求体
    private HashMap<String,String> RequestUrlParameter;//请求参数
    public StringBuilder allRequestStr;//请求到的所有字符串


    //------------------属性 end-----------------------

    //---------------------getter start--------------------------
    public String getUri() {
        return this.uri;
    }

    public String getMethod() {
        return method;
    }

    public String getHttpVersion() {
        return HttpVersion;
    }

    public String getRequestBody() {
        return RequestBody;
    }

    public HashMap<String, String> getRequestParameters() {
        return RequestUrlParameter;
    }

    public HashMap<String, String> getRequestHeaders() {
        return RequestHeaders;
    }

    public String getAllRequestStr() {
        return allRequestStr.toString();
    }


    //---------------------getter end--------------------------


    //---------------setter start-------------------

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setHttpVersion(String httpVersion) {
        this.HttpVersion = httpVersion;
    }

    public void setRequestHeaders(HashMap<String, String> requestHeaders) {
        this.RequestHeaders = requestHeaders;
    }

    public void setRequestBody(String requestBody) {
        this.RequestBody = requestBody;
    }

    public void setRequestParameters(HashMap<String, String> requestParameters) {
        this.RequestUrlParameter = requestParameters;
    }

    public void appendAllRequestStr(String readStr){
        this.allRequestStr.append(readStr);
    }

    //---------------setter end-------------------


}
