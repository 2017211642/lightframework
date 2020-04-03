package org.framework.servlet;

import org.framework.annotation.HttpMethod;
import org.framework.bean.Context;
import org.framework.bean.RouteInfor;

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
        System.out.println("mvc开始");
        //路由解析
        RouteEngine.instacne();
        System.out.println("mvc完成");
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        System.out.print("new request: ");

        String reback = "";
        RouteEngine routeEngine = RouteEngine.getInstance();
        HttpMethod httpMethod = HttpMethod.valueOf(req.getMethod());
        String url = req.getRequestURI();
        String[] content = url.split("\\?");
        String prefix = content[0];
        System.out.print(" 路由: " + prefix+", ");
        if (prefix.equals("/")) {
            res.getWriter().print("hello");
            res.getWriter().close();
            return;
        }
        if (prefix.equals("/favicon.ico")) {
            res.getWriter().print("你好");
            res.getWriter().close();
            return;
        }
        RouteInfor routeInfor = routeEngine.getRoutUrl().get(prefix);
        if (null != routeInfor) {
            HttpMethod defaultMethod = routeInfor.getMethod();
            if (defaultMethod != httpMethod) {
                reback = "405 erro method";
                res.getWriter().print(reback);
                res.getWriter().close();
                return;
            }
            Handler handler = routeEngine.getHandlerMap().get(routeInfor);
            if (handler == null) {
                reback = "404 erro path";
                System.out.print(": 未找到路由,");
                res.getWriter().print(reback);
                res.getWriter().close();
                return;
            } else {
                Context context = new Context(req, res);
                reback = handler.invoke(context);
                res.getWriter().print(reback);
                res.getWriter().close();
            }
        } else {
            System.out.print("路由信息错误");
        }
        System.out.println();
    }
}
