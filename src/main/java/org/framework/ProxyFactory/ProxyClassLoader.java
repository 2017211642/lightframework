package org.framework.ProxyFactory;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ProxyClassLoader {
    private static ProxyClassLoader proxyClassLoader;
    private  static  final String random = "*";
    private Set<Class<?>> tempClassSet = null;
    private Map<Class<?>,Object> classObjectMap = null;
    private Map<Class<?>, Method> classMethod = null;
    private static String path = "";
    private String methodName = "";
    /**
     *
     * 接口暴露，共其他地方使用,比如代理
     *
     * 目的，获取需要被代理的方法的实例
     *
     * */
    public static void instence(String[] packageInfor){
        path = Thread.currentThread().getContextClassLoader().getResource("/").getPath();
        proxyClassLoader = new ProxyClassLoader(packageInfor);
    }
    public static ProxyClassLoader instance(){
        return proxyClassLoader;
    }
    public ProxyClassLoader(String[] packageInfor){
        tempClassSet = new HashSet<>();
        classObjectMap = new HashMap<>();
        classMethod = new HashMap<>();
       loading(packageInfor);
    }
    /**
     * 加载指定路径下的文件
     * */
    public void loading(String[] str) {
        String packagePath = path+ str[0].replaceAll("\\.","/");
        File[] fileList = new File(packagePath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                methodName = str[2];
                if(str[1].equals(random)){
                    return true;
                }else {
                    if (pathname.getName().equals(str[1] + ".class")) {
                        return true;
                    }
                    return false;
                }
            }
        });
        if (fileList != null) {
            for (File file : fileList) {
                try {
                    String fileName = str[0]+"."+file.getName().replace(".class","");
                    Class<?> clazz = Class.forName(fileName);//代理对象的实例
                    tempClassSet.add(clazz);
                    classObjectMap.put(clazz,clazz.newInstance());//被代理对象的实例集合
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("Class Not Funnd");
                } catch (IllegalAccessException  | InstantiationException e) {
                    throw new RuntimeException("Cant not get a class instance");
                }
            }
        }
        loadMethod();
    }
    private void loadMethod(){
        for(Class<?> clazz:tempClassSet){
            Method[] methods = clazz.getMethods();
            for(Method method:methods){
                //这个方法中需要被代理的类的方法
                if(methodName.equals(random)){
                   throw  new RuntimeException("can not be a default:"+random);
                }else {
                    if(methodName.equals(method.getName())){
                        classMethod.put(clazz,method);
                    }
                }
            }
        }
        if(classMethod.size() < 1){
            throw  new RuntimeException("not funnd:"+methodName);
        }
    }
    public Set<Class<?>> getTempClassSet(){
        return tempClassSet;
    }
    public Map<Class<?>,Object> getClassObjectMap(){
        return classObjectMap;
    }
    public Map<Class<?>,Method> getClassMethodMap(){
        return classMethod;
    }
}
