package org.framework.servlet;

import org.framework.annotation.HttpMethod;
import org.framework.aspect.AspectFactory;
import org.framework.bean.Context;
import org.framework.bean.RouteInfor;
import org.framework.core.BeanClassLoader;
import org.framework.core.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebServlet("/*")
public class DispathcerServlet extends HttpServlet {


    @Override
    public void init() {
        System.out.println("加载器初始化开始");
        try {
            PropertieLoader.init();
        //加载 beandefine
        BeanClassLoader.instance();
        //在这里对bean 生成代理来替换
        AspectFactory.instance();
        //实现ioc
        BeanFactory.getIncetance();
        //路由解析
        RouteEngine.instacne();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("加载器初始化完成");
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        System.out.println("new request");
        String reback = "";
        RouteEngine routeEngine = RouteEngine.getInstance();
        HttpMethod httpMethod = HttpMethod.valueOf(req.getMethod());
        String url = req.getRequestURI();
        String[] content = url.split("\\?");
        String prefix = content[0];
        System.out.println("路由:"+prefix);
        if(prefix.equals("/")){
            res.getWriter().print("hello");
            res.getWriter().close();
            return;
        }
        if(prefix.equals("/favicon.ico"))
        {
            System.out.println("请求 ioc");
            res.getWriter().print("你好");
            res.getWriter().close();
            return;
        }
        RouteInfor routeInfor = routeEngine.getRoutUrl().get(prefix);
        HttpMethod defaultMethod = routeInfor.getMethod();
        if(defaultMethod != httpMethod){
            reback = "405 erro method";
            res.getWriter().print(reback);
            res.getWriter().close();
            return;
        }
        Handler handler = routeEngine.getHandlerMap().get(routeInfor);
        if(handler == null){
            reback = "404 erro path";
            System.out.println("未找到");
            res.getWriter().print(reback);
            res.getWriter().close();
            return;
        }else{
            Context context = new Context(req,res);
            reback = handler.invoke(context);
            res.getWriter().print(reback);
            res.getWriter().close();
        }
    }
}
