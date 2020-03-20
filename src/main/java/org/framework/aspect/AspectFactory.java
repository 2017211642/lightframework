package org.framework.aspect;

import org.framework.ProxyFactory.CGlibProxy;
import org.framework.ProxyFactory.JDKProxy;
import org.framework.aspect.annotation.Aspect;
import org.framework.aspect.annotation.PointCut;
import org.framework.core.BeanClassLoader;
import org.framework.demo.TestSer;
import org.framework.demo.TestService;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

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
        System.out.println("代理");
        classSet = BeanClassLoader.instance().getClassSet();
        beanMap = BeanClassLoader.instance().getBeanMap();
        stringBeanMap = BeanClassLoader.instance().getStringBeanMap();
        loadAspect();
        System.out.println("代理完成");
    }

    /***
     *
     * */
    private void loadAspect() throws Exception {
        for (Class<?> clazz : classSet) {
            Annotation[] annotations = clazz.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (annotationType.equals(Aspect.class)) {
                    loadPointCutPagePath(clazz);
                    break;
                }
            }
        }
    }

    /**
     * 加载切面配置
     * 获取切入点信息
     */
    private void loadPointCutPagePath(Class<?> clazz) throws Exception {
        System.out.println("开始替换");
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            System.out.println(method);
            PointCut pointCut = method.getAnnotation(PointCut.class);
            if (pointCut != null) {
                String[] value = pointCut.value();
                StringBuilder targetKey = new StringBuilder(value[0]);
                targetKey.append(".").append(value[1]);
                //获取到被代理对象
                Class<?> targetClass = stringBeanMap.get(targetKey.toString());
                if (null == targetClass) {
                    throw new RuntimeException("被代理对象不存在:" + targetClass);
                }
                Object proxyObject = null;
                if(targetClass.getInterfaces().length>0 ){
                    System.out.println(targetClass.isInterface());
                    System.out.println("接口实现 jdk");
                    Object targetObject = beanMap.get(targetClass);
                    Class<?> interfaceClazz = targetClass.getInterfaces()[0];
                    proxyObject = JDKProxy.getProxy(targetClass,targetObject,interfaceClazz);
                }else{
                    System.out.println("cglib");
                    //Class.forName("net.sf.cglib.proxy.Callback").newInstance();
                    proxyObject = CGlibProxy.getProxy(targetClass);
                }
                System.out.println("proxy:" + proxyObject);
                //替换
                beanMap.put(targetClass, proxyObject);
            }
        }
        System.out.println("替换完成");
    }

    public static void main(String[] args) {
//        try {
//            Class<?> clazz = Class.forName("org.framework.demo.TestService");
//            Object o = JDKProxy.getProxy(clazz,new TestService(), TestSer.class);
//            System.out.println(o);
//            TestSer t = (TestSer)o;
//            TestService testService = (TestService)t;
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
