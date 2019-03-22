package org.framework.aspect;

import org.framework.ProxyFactory.ProxyClassLoader;
import org.framework.aspect.annotation.Aspect;
import org.framework.aspect.annotation.PointCut;
import org.framework.core.BeanFactory;
import org.framework.core.ClassLoader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * 获取所有切面配置文件
 *
 * */
public class AspectFactory {
    private static  AspectFactory aspectFactory;
    private Set<Class<?>> classSet;
    private Set<Class<?>> aspectSet;
    private Set<String[]> packagePathSet;
    private Map<String,Class<?>> aspectMap;
    public static void instance(){
        aspectFactory = new AspectFactory();
    }
    public AspectFactory(){
        classSet = ClassLoader.instance().getClassSet();
        aspectSet = new HashSet<>();
        aspectMap = new HashMap<>();
        packagePathSet = new HashSet<>();
       loadAspect();
    }

    /***
     *
     * */
    private void loadAspect(){
        for(Class<?> clazz:classSet){
            Annotation[] annotations = clazz.getDeclaredAnnotations();
            for(Annotation annotation:annotations){
                Class<? extends Annotation>  annotationType = annotation.annotationType();
                if(annotationType.equals(Aspect.class)){
                    aspectSet.add(clazz);
                    break;
                }
            }
        }
        loadPointCutPagePath();
    }
    /**
     * 加载切面配置
     *
     * */
    private void loadPointCutPagePath(){
        for(Class<?> clazz:aspectSet){
            Method[] methods = clazz.getMethods();
            for(Method method:methods){
                PointCut pointCut = method.getAnnotation(PointCut.class);
                if(pointCut != null){
                    String[] value = pointCut.value();
                    packagePathSet.add(value);
                    aspectMap.put(value[1],clazz);
                }
            }
        }
        proxyLinkPoint();
    }
/**
 *
 * 反射，即获取连接点的代理权
 *
 */
    private void proxyLinkPoint(){
        for(String[] packagePath:packagePathSet){
            ProxyClassLoader.instence(packagePath);
        }
    }

}
