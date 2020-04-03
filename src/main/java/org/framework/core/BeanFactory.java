package org.framework.core;


import org.framework.annotation.Autowired;
import org.framework.annotation.Service;
import org.framework.util.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/***
 *
 * 类的分配
 *1.classLoader 进行类的加载，装进classSet
 * 2.在这个类中对classSet 中的类做进一步解析，
 * 3.即查看注解，对对应的注解实现注入
 * */
public class BeanFactory {
    public static BeanFactory beanFactory; //单例
    private ConfigClassLoader configClassLoader;//类加载器实例
    private Set<Class<?>> classSet; //所有类的集合
    private Map<Class<?>, Object> beanMap;
    private Map<Class<?>, Object> service; //service
    private Map<Class<?>, Object> controller; //controller

    public synchronized static BeanFactory getIncetance() {
        if (beanFactory == null) {
            beanFactory = new BeanFactory();
        }
        return beanFactory;
    }

    public Map<Class<?>, Object> getControllerMap() {
        return controller;
    }

    public BeanFactory() {
        //获取加载器实例
        configClassLoader = ConfigClassLoader.instance();
        classSet = configClassLoader.getClassSet();
        //保存对象实例
        beanMap = configClassLoader.getBeanMap();
        service = new HashMap<>();
        controller = new HashMap<>();
        init();
        serviceIoc();
        controllerIoc();
    }

    /**
     * 对类进行注解分类，以便接下来的参数注入用
     */
    private void init() {
        for (Map.Entry<Class<?>, Object> entry : beanMap.entrySet()) {
            Class<?> clazz = entry.getKey();
            Object o = entry.getValue();
            Annotation[] annotations = clazz.getAnnotations(); //获取某个类的全部注解
            for (Annotation annotation : annotations) {
                //查看类级别的注解
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (annotationType.equals(Service.class) || annotationType.isAnnotationPresent(Service.class)) {
                    //不管是 controller 还是 其他地方注入 都到service 里面找
                    service.put(clazz, o);
                } else {
                    //主要是在mvc 阶段使用，controller 就是 handler
                    controller.put(clazz, o);
                }
            }
        }
    }

    /**
     * service 的注入
     * */
    private void serviceIoc(){
        Set<Map.Entry<Class<?>, Object>> serviceSet = service.entrySet();
        for (Map.Entry<Class<?>,Object> entry : serviceSet) {
            Class<?> clazz = entry.getKey();
            //获取对向成员参数
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                Autowired autowired = field.getAnnotation(Autowired.class);
                //如果这个成员有注入标识
                if (autowired == null) continue;
                //根据成员类型获取对应的实例
                Object target = service.get(field.getType());
                if (target == null) continue;
                if(field.getType() == clazz) throw new RuntimeException("自己不能注入自己");
                //被注入对象实例
                Object object = entry.getValue();
                //ioc,object 是用来指定是哪个类，target 是将要注入的参数实例 ，这里的field 是目标对象
                setFileValue(field, object, target);
            }
        }
    }
    /**
     * 对controller 进行注入
     */
    private void controllerIoc() {
        Set<Map.Entry<Class<?>, Object>> controllerSet = controller.entrySet();
        for (Map.Entry<Class<?>,Object> entry: controllerSet) {
            Class<?> clazz = entry.getKey();
            //获取对向成员参数
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                Autowired autowired = field.getAnnotation(Autowired.class);
                //如果这个成员有注入标识
                if (autowired == null) continue;
                //根据成员类型获取对应的实例
                Object target = service.get(field.getType());
                if (target == null) continue;
                //被注入对象实例
                Object object = entry.getValue();
                //ioc,object 是用来指定是哪个类，target 是将要注入的参数实例 ，这里的field 是目标对象
                setFileValue(field, object, target);
            }
        }
    }

    /**
     * 对参数进行注入，
     * 1.先看是否有权限，有权限就设置为忽略权限
     * 2.把获取到的参数实例注入，实现自动new 一个对象
     */
    private void setFileValue(Field field, Object clazz, Object fieldInstance) {
        try {
            //私有对象
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            field.set(clazz, fieldInstance);
        } catch (IllegalAccessException e) {
            System.out.println("Erro type for Autowired");
        }
    }
}
