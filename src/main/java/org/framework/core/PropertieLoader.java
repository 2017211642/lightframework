package org.framework.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
/**
 *
 * 想法太高，能力太弱。
 *
 * 1.实现简单的配置文件读取：
 * 把配置文件放到指定的地方，便于读取
 * 配置文件都会被加载到target 的classes 地方
 *
 * 2.目的：读取所有class 的父包路径，便于类加载器去加载类 ：
 * */
public class PropertieLoader {

    private static Set<String> filePath = new HashSet<>();//保存配置从配置文件读取的包路径
    private static PropertieLoader propertieLoader;//实例
    private static Properties properties;//配置文件,需要被扫描的包路径
    /**
     *
     * 初始化配置
     */
    public  synchronized  static void init(){
        propertieLoader = new PropertieLoader();
        getPropertis();//获取配置文件路径
        initPropertis();//加载配置文件
    }
    /**
     *
     * 获取配置文件路径
     * */

    public static void getPropertis() {
        String path = Thread.currentThread().getContextClassLoader().getResource("/").getPath();//获取真实路径
        File[] files = new File(path).listFiles(file -> !file.isDirectory());
            for (File f : files) {
                filePath.add(f.getPath());
            }
    }
    /**
     * 加载配置文件
     * */
    public static void initPropertis(){
        properties = new Properties();
        for(String path:filePath) {
            File file = new File(path);
            InputStream inputStream = null;
            if (file.exists()){
            try {
                inputStream = new FileInputStream(file);
                if (inputStream != null) {
                    properties.load(inputStream);
                }
            } catch (IOException e) {
                        System.out.println("is not propertis");
                    }
            }
         }
    }

    /**
     * 其他地方获取pro实例
     * */
    public static PropertieLoader getInstance(){
        return propertieLoader;
    }
    public  Properties getPropertiesInstance(){
        return properties;
    }
}
