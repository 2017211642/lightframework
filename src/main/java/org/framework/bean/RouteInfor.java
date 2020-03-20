package org.framework.bean;

import org.framework.annotation.HttpMethod;

/**
 *
 * 记录controller的路由信息
 * httpMethod  请求方法（get ,post ,and so on）
 * url  请求的url
 * urlParams  url 携带的参数
 *
 * */
public class RouteInfor {
    private HttpMethod httpMethod;
    private String url;


    public HttpMethod getMethod() {
        return httpMethod;
    }

    public void setMethod(HttpMethod method) {
        this.httpMethod = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
public RouteInfor(){

}
public RouteInfor(String url, HttpMethod httpMethod){
        this.url = url;
        this.httpMethod = httpMethod;
}
}
