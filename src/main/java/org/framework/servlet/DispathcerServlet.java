package org.framework.servlet;

import org.framework.annotation.HttpMethod;
import org.framework.aspect.AspectFactory;
import org.framework.bean.Context;
import org.framework.bean.RouteInfor;
import org.framework.core.*;
import org.framework.core.ClassLoader;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

//@WebServlet("/*")
public class DispathcerServlet extends HttpServlet {


    @Override
    public void init() throws ServletException {
        System.out.println("加载器初始化开始");
        PropertieLoader.init();
        ClassLoader.instance();
        BeanFactory.getIncetance();
        RouteEngine.instacne();
        AspectFactory.instance();
        System.out.println("加载器初始化完成");
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String reback = "";
        RouteEngine routeEngine = RouteEngine.getInstance();
        HttpMethod httpMethod = HttpMethod.valueOf(req.getMethod());
        String url = req.getRequestURI();
        String[] content = url.split("\\?");
        String prefix = content[0];
        if(prefix.equals("/")){
            res.getWriter().print("你好");
            res.getWriter().close();
            return;
        }
        if(prefix.equals("/favicon.ico"))
        {
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
