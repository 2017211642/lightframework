package org.framework.util;

public class ReflectUtil {
    public static Object newInstance(Class<?> clazz){
        Object object = null;
        try {
            object = clazz.newInstance();
        } catch (InstantiationException e) {
            System.out.println("add class insatance false");
        } catch (IllegalAccessException e) {
            System.out.println("add class insatance false");
        }
        return object;
    }
}
