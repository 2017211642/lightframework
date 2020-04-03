package org.framework.ProxyFactory.jdk;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class JDKProxy {
    public static Object getProxy(Class<?> clazz,Object targetObject,Class<?> interfaceClass) throws Exception {
        // 2、获取动态代理类
        Class proxyClazz = Proxy.getProxyClass(clazz.getClassLoader(),interfaceClass);
        // 3、获得代理类的构造函数，并传入参数类型InvocationHandler.class
        Constructor constructor = proxyClazz.getConstructor(InvocationHandler.class);
        // 4、通过构造函数来创建动态代理对象，将自定义的InvocationHandler实例传入
        Object o = constructor.newInstance(new MyJDKInvocationHandler(targetObject));
        return o;
    }
}
