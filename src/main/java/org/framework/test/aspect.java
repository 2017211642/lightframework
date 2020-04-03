package org.framework.test;

import org.framework.ProxyFactory.cglib.AspectProxy;
import org.framework.aspect.annotation.Aspect;
import org.framework.aspect.annotation.PointCut;

@Aspect
public class aspect extends AspectProxy {
    @PointCut({"org.framework.demo","TestService","hello"})
    public void testMethod(){

    }

    @Override
    public void before(){
        System.out.println("before");
    }

    @Override
    public void after(){
        System.out.println("after");
    }


}
