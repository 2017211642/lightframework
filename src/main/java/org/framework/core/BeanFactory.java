package org.framework.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.framework.annotation.Autowired;
import org.framework.annotation.Service;
import org.framework.annotation.Controller;
import org.framework.util.ReflectUtil;


/***
 *
 * 类的分配
 *1.classLoader 进行类的加载，装进classSet
 * 2.在这个类中对classSet 中的类做进一步解析，
 * 3.即查看注解，对对应的注解实现注入
 * */
public class BeanFactory {
    public static BeanFactory beanFactory; //单例
    private Set<Class<?>> classSet; //所有类的集合
    private Map<Class<?>,Object> service ; //service
    private Set<Class<?>> serviceSet;
    private  Map<Class<?>,Object> controller; //controller
    private Set<Class<?>> controllerSet;
    private ClassLoader classLoader;//类加载器实例
    public static BeanFactory getIncetance(){
        if(beanFactory == null) {
            beanFactory = new BeanFactory();
        }
        return  beanFactory;
    }
    public  Map<Class<?>,Object> getControllerMap(){
        return controller;
    }
    public Set<Class<?>> getControllerSet(){
        return controllerSet;
    }
    public BeanFactory(){
        classSet = new HashSet<>();
        classLoader = ClassLoader.instance();
        service = new HashMap<>();
        controller = new HashMap<>();
        controllerSet = new HashSet<>();
        serviceSet = new HashSet<>();
        classSet = classLoader.getClassSet();
        init();
        controllerIoc();
    }
    /**
     *
     * 对类进行注解分类，以便接下来的参数注入用
     * */
    private void init(){
        for(Class<?> clazz : classSet){
          //  System.out.println(clazz);
            Annotation[] annotations = clazz.getAnnotations(); //获取某个类的全部注解
            for(Annotation annotation :annotations){
                //查看类级别的注解
             Class<? extends Annotation>  annotationType  =   annotation.annotationType();
             if(annotationType.equals(Service.class) || annotationType.isAnnotationPresent(Service.class)){
                // System.out.println("service :"+clazz);
                 Object classInstance = ReflectUtil.newInstance(clazz);
                  service.put(clazz,classInstance);
                  serviceSet.add(clazz);
             }else{
               //  System.out.println("controller :"+clazz);
                 Object classInstance = ReflectUtil.newInstance(clazz);
                 controller.put(clazz,classInstance);
                 controllerSet.add(clazz);
             }
            }
        }
    }

    /**
     *
     * 对controller 进行注入
     * */
    private void controllerIoc(){

        for(Class<?> clazz : controllerSet){
            Field[] fields = clazz.getDeclaredFields();
            for(Field field : fields){
                Autowired autowired = field.getAnnotation(Autowired.class);
                //如果这个成员有注入标识
                if(autowired != null){
                    //根据成员类型获取对应的实例
                    Object target = service.get(field.getType());
                    if(target != null){
                        Object object = controller.get(clazz); //目标类的实例
                       //ioc,object 是用来指定是哪个类，target 是将要注入的参数实例 ，这里的field 是目标对象
                        setFileValue(field,object,target);
                    }
                }
            }
        }
    }
    /**
     *
     * 对参数进行注入，
     * 1.先看是否有权限，有权限就设置为忽略权限
     * 2.把获取到的参数实例注入，实现自动new 一个对象
     * */
    private void setFileValue(Field field,Object clazz,Object fieldInstance){
        try {
            //私有对象
            if(!field.isAccessible()){
                field.setAccessible(true);
            }
            field.set(clazz,fieldInstance);
        } catch (IllegalAccessException e) {
            System.out.println("erro type for auto");
        }
    }
}
