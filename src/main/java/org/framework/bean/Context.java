package org.framework.bean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/***
 *  上下文
 * */
public class Context {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Map<String,Object> paramsMap ;

    public Context(){

    }
    public Context(HttpServletRequest request,HttpServletResponse response){
        this.paramsMap = new HashMap<>();
        this.request = request;
        this.response = response;
    }
    public Map<String,Object> getParams(){
        return paramsMap;
    }
    public HttpServletRequest getRequest() {
        return request;
    }


    public HttpServletResponse getResponse() {
        return response;
    }

    public Object getParam(String key){
        return paramsMap.get(key);
    }
    public void setParamsMap(String key,Object value) {
        this.paramsMap.put(key,value);
    }

}
