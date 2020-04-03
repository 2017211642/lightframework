package org.framework.ProxyFactory.cglib;

import org.framework.ProxyFactory.cglib.Proxy;
import org.framework.ProxyFactory.cglib.ProxyChain;

import java.lang.reflect.Method;

public class AspectProxy implements Proxy {
    @Override
    public final Object doProxy(ProxyChain proxyChain) {
        Object result = null;
        Class<?> cls = proxyChain.getTargetClass();
        Method method = proxyChain.getTargetMethod();
        Object[] params = proxyChain.getMethodParams();
        //开始
        before();
        try{
            result = proxyChain.doProxyChain();
            after();
        }catch (Exception e){
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }finally {

        }
        return result;
    }

    public void before(){

    }

    public void after(){

    }

}
