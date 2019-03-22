package org.framework.core;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Annotated;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileFilter;
import java.util.*;

/**
 *
 * 类加载器,根据配置文件加载指定包下的class 文件
 *
 * 1.获取所有类
 *
 * */
public class ClassLoader {
    private static ClassLoader classLoader; //单例
    private   Set<Class<?>> classSet ;//所有类的集合
    public static ClassLoader instance(){
        if(classLoader == null) {
            classLoader = new ClassLoader(PropertieLoader.getInstance());
        }
        return classLoader;
    }
    public ClassLoader(PropertieLoader propertieLoader){
        loadClass(propertieLoader);
    }
    public Set<Class<?>> getClassSet(){
        return classSet;
    }

    /**
     *
     * 2.根据配置文件获取类的包名
     *
     * */
    private void loadClass(PropertieLoader propertieLoader){
        //生成类集合的实例
        classSet = new HashSet<>();
        //获取加载的配置文件
        Properties properties = propertieLoader.getPropertiesInstance();
        //获取配置文件的key集合
        Enumeration propertie =  properties.propertyNames();
       while (propertie.hasMoreElements()){
           //获取key
           String key = propertie.nextElement().toString();
           //获取包
           String packageName= properties.getProperty(key);
           try {
               //获取项目真实路径
              String projectPath = Thread.currentThread().getContextClassLoader().getResource("/").getPath();
              loadAllClassFile(packageName,projectPath);
           } catch (ClassNotFoundException e) {
               e.printStackTrace();
           }
       }
    }

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
   private void loadAllClassFile(String pakegeName, String projectPath) throws ClassNotFoundException {
        //组合获取文件路径
        String filePath = toLString(projectPath,pakegeName.replaceAll("\\.","/"));
        //文件筛选,只要文件夹和.class 文件
       File[] classFile = new File(filePath).listFiles(new FileFilter() {
           @Override
           public boolean accept(File pathname) {
               if(pathname.isDirectory() || pathname.getName().endsWith(".class")){
                   return true;
               }
               return false;
           }
       });
       if (classFile != null) {
           for (File file : classFile) {
               String fileName = file.getName();
               if (file.exists()) {
                   if (file.isDirectory()) {
                       //是文件夹的话就递归遍历
                       loadAllClassFile(toLString(pakegeName,".",fileName),projectPath);
                   } else {
                       //类文件就获取这个类的实例，添加进类的集合
                       Class<?> clazz = null;
                       String className = toLString(pakegeName,".",fileName).replace(".class","");
                       try {
                           clazz = Class.forName(className);
                       } catch (ClassNotFoundException e) {
                           System.err.println("找不到指定包文件:"+className);
                       }
                       if (clazz != null) {
                           //添加进类的集合
                           classSet.add(clazz);
                       }
                   }
               }
           }
       }
   }
   private String toLString(String ...args){
        StringBuilder stringBuilder = new StringBuilder();
        for(String str:args){
            stringBuilder.append(str);
        }
        return  stringBuilder.toString();
   }

   @Test
   public void test(){
       File[] filee = new File("G:\\").listFiles();
       System.out.println(filee.length);
   }
}
