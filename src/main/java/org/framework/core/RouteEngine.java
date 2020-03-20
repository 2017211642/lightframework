package org.framework.core;

import org.framework.annotation.HttpMethod;
import org.framework.annotation.RequestMapping;
import org.framework.bean.RouteInfor;
import org.framework.util.SetParameModelUtil;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 路由加载
 *
 * */
public class RouteEngine {

    private static RouteEngine routeEngine;//单例

    private static  Map<RouteInfor, Handler> handlerMap = null; //单例模式
    private static Map<String, RouteInfor> routUrl = null;//
    private BeanFactory beanFactory = null;
    public synchronized static void instacne(){
        if(routeEngine == null) {
            routeEngine = new RouteEngine();
        }
        System.out.println("route:"+routUrl.size());
    }
    public static RouteEngine getInstance(){
        return  routeEngine;
    }
public RouteEngine(){
        routUrl = new HashMap<>();//保存routInfor 信息
        handlerMap = new HashMap<>(); //单例模式
        load();
  }
  public   Map<RouteInfor, Handler>  getHandlerMap(){
        return  handlerMap;
  }
  public Map<String, RouteInfor> getRoutUrl(){
        return routUrl;
  }
  /**
   *
   对controller requestMapping 进行解析

   */
 void load (){
     System.out.println("loading...."+System.currentTimeMillis());
     beanFactory = BeanFactory.getIncetance();//bean工厂实例
     Map<Class<?>,Object> controllerMap = beanFactory.getControllerMap();
     Set<Class<?>> controllerSet = beanFactory.getControllerMap().keySet();
     for(Class<?> clazz :controllerSet){
         String prefix = "";
         if(null != clazz){
             System.out.println("实例:"+clazz.getName());

             //看这个类是否有这个注解
             RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
             if(requestMapping != null){
                 prefix += requestMapping.value();
             }
             System.out.println(prefix);
             //获取这类里面的所有方法
             Method[] methods = clazz.getMethods();
             for(Method method:methods){
                 RequestMapping requestMapper = method.getAnnotation(RequestMapping.class);
                 if(requestMapper != null){
                     //路由
                     prefix += requestMapper.value();
                     HttpMethod httpMethod = requestMapper.method();
                     RouteInfor routeInfor = new RouteInfor(prefix,httpMethod); //设置这个方法的路由信息
                     Handler handler = new Handler(controllerMap.get(clazz),method);
                     String[] paramsName = SetParameModelUtil.getMethodParameterNamesByAsm4(clazz,method);
                     handler.setParamsName(paramsName);//保存类的参数的名字,按顺序保存的
                     routUrl.put(prefix,routeInfor);
                     handlerMap.put(routeInfor,handler);//添加进路由表
                 }
             }
         }
     }
     System.out.println("endl...."+System.currentTimeMillis());
 }
}
