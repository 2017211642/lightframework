package org.framework.ProxyFactory.cglib;

import net.sf.cglib.proxy.MethodProxy;
import org.framework.ProxyFactory.cglib.Proxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ProxyChain {
    private  final  Class<?> targetClass;
    private final Object targetObject;
    private final Method targetMethod;
    private final MethodProxy methodProxy;
    private final Object[] methodParams;
    private int methodIndex = 0;

    private List<Proxy> proxyList = new ArrayList<Proxy>();

    public ProxyChain(Class<?> targetClass, Object targetObject, Method targetMethod, MethodProxy methodProxy, Object[] methodParams,List<Proxy> list) {
        this.targetClass = targetClass;
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.methodProxy = methodProxy;
        this.methodParams = methodParams;
        this.proxyList = list;
    }


    public Object doProxyChain() throws Throwable {
        System.out.println("doProxyChain");
        Object methodResult;
        if(methodIndex < proxyList.size()){
            methodResult = proxyList.get(methodIndex++).doProxy(this);
        }else{
            methodResult = methodProxy.invokeSuper(targetObject,methodParams);
        }
        return methodResult;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public Object[] getMethodParams() {
        return methodParams;
    }

    public Class<?> getTargetClass(){
        return  this.targetClass;
    }

}
