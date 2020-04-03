package org.framework.ProxyFactory.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.framework.ProxyFactory.cglib.Proxy;
import org.framework.ProxyFactory.cglib.ProxyChain;

import java.lang.reflect.Method;
import java.util.List;

public class ProxyManager {
    public static <T> T createProxy(final  Class<?> target,final List<Proxy> proxyList){
        return (T) Enhancer.create(target, new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                //代理回调这个方法
                 return new ProxyChain(target,o,method,methodProxy,objects,proxyList).doProxyChain();
            }
        });
    }
}
