package org.framework.test;

import org.framework.ProxyFactory.CGlibProxy;
import org.framework.aspect.annotation.Aspect;
import org.framework.aspect.annotation.PointCut;
import org.junit.jupiter.api.Test;

@Aspect
public class aspect {
    @PointCut({"org.framework.test","test","hello"})
    public void testMethod(){
    }

    @Test
   public void test(){
       CGlibProxy cGlibProxy = new CGlibProxy();
       test hello = cGlibProxy.getProxy(test.class);
       hello.hello();
   }
}
