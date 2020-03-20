package org.framework.ProxyFactory;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class BeanProxy  implements MethodInterceptor {

    public BeanProxy(){
        System.out.println("实例化");
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        before();
        //获取代理对象，没有传对象接口了
        Object result = methodProxy.invokeSuper(o,objects);
        after();
        return result;
    }

    private void before() {
        System.out.println("before");
    }

    private void after() {
        System.out.println("after");
    }
}
