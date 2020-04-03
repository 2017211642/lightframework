package org.framework.core;

import org.framework.aspect.AspectFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class StartListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("加载器初始化开始");
        try {
            PropertieLoader.init();
            //加载 beandefine
            ConfigClassLoader.instance();
            //在这里对bean 生成代理来替换
            AspectFactory.instance();
            //实现ioc
            BeanFactory.getIncetance();
//            //路由解析
//            RouteEngine.instacne();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("加载完成");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
