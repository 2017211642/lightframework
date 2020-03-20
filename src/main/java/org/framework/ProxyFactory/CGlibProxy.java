package org.framework.ProxyFactory;

import net.sf.cglib.proxy.Enhancer;
import org.framework.demo.TestService;

public class CGlibProxy {


    public static void print() {
        System.out.println("test");
    }
    /**
     * 生成CGLIB代理对象
     * @param cls Class类
     * @return Class类的CGLIB代理对象
     */
    public static <T> T getProxy(Class cls) {
        System.out.println("被代理:"+cls);
        print();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(cls);
        enhancer.setCallback(new BeanProxy());
        enhancer.setClassLoader(cls.getClassLoader());
        // 生成并返回代理对象
        return (T)enhancer.create();
    }

//    public static void main(String[] args) throws ClassNotFoundException {
//        Class<?> clazz = Class.forName("org.framework.demo.TestService");
//        TestService testService = (TestService) new BeanProxy().getProxy(clazz);
//        testService.hello();
//    }

}
