package org.framework.ProxyFactory;

import org.framework.aspect.AspectFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * 这里对拿到的代理对象做处理s
 * 1.注入的地方就注入代理对象
 * 2.被代理对象注入到切面的方法
 * 3.把代理者注入到目标地方
 * 4. GG
 * */
public class ProxyFactory {

    private Class<?> target; //代理对象
    private Object[] objects;
    public Object instance(Class<?> clazz,Object... objects){
        this.target = clazz;
        this.objects = objects;
        return this;
    }
    public void test(){
        getProxyInstance();
    }
    public void getProxyInstance(){
        //代理的方法
        Method method = ProxyClassLoader.instance().getClassMethodMap().get(target);
        //执行目标方法
        try {
            method.invoke(target,objects);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
