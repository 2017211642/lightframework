package org.framework.aspect;

import org.framework.ProxyFactory.cglib.Proxy;
import org.framework.ProxyFactory.cglib.ProxyManager;
import org.framework.ProxyFactory.jdk.JDKProxy;
import org.framework.aspect.annotation.Aspect;
import org.framework.aspect.annotation.PointCut;
import org.framework.core.ConfigClassLoader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import java.util.*;

/**
 * 获取所有切面配置文件
 */
public class AspectFactory {
    private static AspectFactory aspectFactory;
    //类集合
    private Set<Class<?>> classSet;
    private Map<Class<?>, Object> beanMap;
    private Map<String, Class<?>> stringBeanMap;
    private Class<?> proxyClass;

    public synchronized static void instance() throws Exception {
        aspectFactory = new AspectFactory();
    }

    public AspectFactory() throws Exception {
        System.out.println("代理开始");
        classSet = ConfigClassLoader.instance().getClassSet();
        stringBeanMap = ConfigClassLoader.instance().getBeanNameMap();
        loadAspect();
        System.out.println("代理完成");
    }

    /***
     *
     * */
    private void loadAspect() throws Exception {
        System.out.println("代理中");
        for (Class<?> clazz : classSet) {
            Annotation[] annotations = clazz.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (!annotationType.equals(Aspect.class)) continue;
                loadPointCutPagePath(clazz);
            }
        }

    }

    /**
     * 加载切面配置
     * 获取切入点信息
     */
    private void loadPointCutPagePath(Class<?> clazz) throws Exception {

        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            PointCut pointCut = method.getAnnotation(PointCut.class);
            if (pointCut == null) continue;
            String[] value = pointCut.value();
            StringBuilder targetKey = new StringBuilder(value[0]);
            targetKey.append(".").append(value[1]);
            //获取到被代理对象
            Class<?> targetClass = stringBeanMap.get(targetKey.toString());
            if (null == targetClass) throw new RuntimeException("被代理对象不存在:" + targetClass);
            Object proxyObject = null;
            if (targetClass.getInterfaces().length > 0) {
                Object targetObject = beanMap.get(targetClass);
                Class<?> interfaceClazz = targetClass.getInterfaces()[0];
                proxyObject = JDKProxy.getProxy(targetClass, targetObject, interfaceClazz);
            } else {
                    System.out.println("cglib");
                    //Class.forName("net.sf.cglib.proxy.Callback").newInstance();
                    Proxy proxy = (Proxy)clazz.newInstance();
                    List<Proxy> list = new ArrayList<Proxy>();
                    list.add(proxy);
                    System.out.println("creating...");
                    //proxyObject = ProxyManager.createProxy(targetClass,list);
            }
            if(proxyObject != null) beanMap.put(targetClass, proxyObject);
        }
    }
}
