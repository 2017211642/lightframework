package org.framework.core;

import org.framework.util.ReflectUtil;

import java.io.File;
import java.io.FileFilter;
import java.util.*;

/**
 * ###只要配置文件设置了路径，这里就会把类给扫描并保存
 *
 * 1.根据包名和文件路径去加载这个文件夹
 * 2.查看这个文件夹里面的class 文件，
 * 3.获取类名字，根据反射规则形成需要的包名
 * 4.根据反射机制获取类的对象,即类的实例
 * 5.类的实例里面包含了类的所有信息
 *
 * */
public class ConfigClassLoader {
    private static ConfigClassLoader configClassLoader; //单例
    private   Set<Class<?>> classSet ;//所有类的集合
    private   Map<Class<?>,Object> beanMap;// 真正的ioc 容器
    private   Map<String,Class<?>> beanNameMap;
    private   Map<String,String> beanPackage;
    public Set<Class<?>> getClassSet(){
        return classSet;
    }

    public Map<Class<?>,Object> getBeanMap(){
        return beanMap;
    }

    public Map<String,Class<?>> getBeanNameMap(){
        return beanNameMap;
    }

    public  static ConfigClassLoader instance(){
        if(configClassLoader == null) {
            synchronized(ConfigClassLoader.class) {
                if(configClassLoader == null) {
                    configClassLoader = new ConfigClassLoader(PropertieLoader.getInstance());
                }
            }
        }
        return configClassLoader;
    }
    public ConfigClassLoader(PropertieLoader propertieLoader){
        try {
        //生成类集合的实例
        classSet = new HashSet<Class<?>>();
        //保存对象实例
        beanMap = new HashMap<>();
        beanNameMap = new HashMap<>();
        beanPackage = new HashMap<>();
        //加载配置文件获取包名
        parseConfigFileGetPackageNames(propertieLoader);
        //解析配置文件
            parseClazz();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * 2.根据配置文件获取类的包名
     *
     * */
    private void parseConfigFileGetPackageNames(PropertieLoader propertieLoader){

        //获取加载的配置文件
        Properties properties = propertieLoader.getPropertiesInstance();
        //获取配置文件的key集合
        Enumeration propertie =  properties.propertyNames();
        //获取项目获真实路径
        String projectPath = Thread.currentThread().getContextClassLoader().getResource("/").getPath();

       while (propertie.hasMoreElements()){
           //获取key
           String key = propertie.nextElement().toString();
           //获取包
           String packageName= properties.getProperty(key);
           beanPackage.put(packageName,projectPath);
       }
    }


   private void parseClazz() throws ClassNotFoundException {
       for (Map.Entry<String, String> entry : beanPackage.entrySet()) {
           String k = entry.getKey();
           String v = entry.getValue();
           loadAllClass(k,v);
       }
   }

   private void loadAllClass(String pakegeName, String projectPath) throws ClassNotFoundException {
        //组合获取文件路径
        String filePath = toLString(projectPath,pakegeName.replaceAll("\\.","/"));
        //文件筛选,只要文件夹和.class 文件
       File[] classFile = fileFilter(filePath);
       if (null == classFile) return;
       for (File file : classFile) {
           if (!file.exists()) continue;
           String fileName = file.getName();
           if (file.isDirectory()) {
               //是文件夹的话就递归遍历
               loadAllClass(toLString(pakegeName,".",fileName),projectPath);
           } else {
               loadClazz(pakegeName,fileName);
           }
       }
   }
   /**
    * 筛选,只要文件夹和.class 文件
    *
    * */
   private File[] fileFilter(String filePath){

       File[] classFile = new File(filePath).listFiles(new FileFilter() {
           @Override
           public boolean accept(File pathname) {
               if(pathname.isDirectory() || pathname.getName().endsWith(".class")){
                   return true;
               }
               return false;
           }
       });

       return classFile;
   }

   /**
    * 通过类全限定名反射加载类,
    *
    * */
   private void loadClazz(String packageName,String clazzName){
       Class<?> clazz = null;
       String className = toLString(packageName,".",clazzName).replace(".class","");
       try {
          //反射获取 class 文件，类加载过程，没有初始化
           clazz = Class.forName(className);
           if(null == clazz) return;
           //添加进类的集合
           classSet.add(clazz);
           Object classInstance = ReflectUtil.newInstance(clazz);
           beanNameMap.put(className,clazz);
           beanMap.put(clazz,classInstance);

       } catch (ClassNotFoundException e) {
           System.err.println("找不到指定包文件:"+className);
       }
   }
   private String toLString(String ...args){
        StringBuilder stringBuilder = new StringBuilder();
        for(String str:args){
            stringBuilder.append(str);
        }
        return  stringBuilder.toString();
   }

}
