package org.framework.core;

import org.framework.annotation.HttpMethod;
import org.framework.bean.Context;
import org.framework.bean.ResponeBody;
import org.framework.bean.RouteInfor;
import org.framework.util.ResponeUtil;
import org.framework.util.SetParameModelUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.TypeVariable;
import java.util.Enumeration;
import java.util.List;

/**
 * 映射器
 *
 * controller 要操作的对象
 * method  用来处理逻辑的方法
 *
 * */
public class Handler {
    private Object controller;
    private Method method;
    private String[] paramsName;

    public String[] getParamsName() {
        return paramsName;
    }

    public void setParamsName(String[] paramsName) {
        this.paramsName = paramsName;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
    public Handler(){

    }
    public Handler(Object controller,Method method){
        this.controller = controller;
        this.method = method;
    }
    public String  invoke(Context c ){
        if(c == null){
            return "404 erro";
        }else{
            HttpServletRequest request = c.getRequest();
            HttpServletResponse response = c.getResponse();
            //获取请求参数
            Enumeration<String> params = request.getParameterNames();
          while (params.hasMoreElements()) {
              String key = params.nextElement();
              Object value = request.getParameter(key);
              //先保存参数，有注解的别名绑定需要用到
              c.setParamsMap(key, value);
          }
                   Class<?>[] enumeration   = method.getParameterTypes();
           // ResponeBody responeBody = null;
            Object  o = null;
            try {
                Object[] objects = new Object[enumeration.length];
                objects = SetParameModelUtil.toObjectArrys(request,response,this,c.getParams(),method,c,objects);
                System.out.println("get params :"+objects);
                if(objects == null && c.getParams().size() > 0){
                    return "405 erro";
                }else if(objects == null && c.getParams().size() == 0) {
                    System.out.println("no params");
                    o = method.invoke(controller);
                }else{
                    System.out.println("hava params");
                    o = method.invoke(controller,objects);
                }
               // responeBody = (ResponeBody)o;
            } catch (Exception e) {
               //return o.toString();
                System.out.println(e.getCause());
                return "return default type";
            }
            return o.toString();
        }
    }
}
