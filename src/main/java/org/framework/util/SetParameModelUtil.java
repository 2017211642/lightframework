package org.framework.util;

import jdk.internal.org.objectweb.asm.*;
import org.framework.bean.Context;
import org.framework.core.Handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SetParameModelUtil {

    private static  Map<Method, Type[]> paramTpyes = new HashMap<>();
    /***
     *
     * 网上copy 大牛的
     * 据说spring 用的就是这个包asm
     *
     * */
    public static String[] getMethodParameterNamesByAsm4(Class<?> clazz, final Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes ==  null || parameterTypes.length == 0) {
            return null;
        }
        final Type[] types = new Type[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            types[i] = Type.getType(parameterTypes[i]);
        }
        final String[] parameterNames = new String[parameterTypes.length];

        String className = clazz.getName();
        int lastDotIndex = className.lastIndexOf(".");
        className = className.substring(lastDotIndex + 1) + ".class";
        InputStream is = clazz.getResourceAsStream(className);
        try {
            ClassReader classReader = new ClassReader(is);
            classReader.accept(new ClassVisitor(Opcodes.ASM4) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    // 只处理指定的方法
                    Type[] argumentTypes = Type.getArgumentTypes(desc);
                    if (!method.getName().equals(name) || !Arrays.equals(argumentTypes, types)) {
                        return null;
                    }
                    paramTpyes.put(method,argumentTypes);
                    return new MethodVisitor(Opcodes.ASM4) {
                        @Override
                        public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                            // 静态方法第一个参数就是方法的参数，如果是实例方法，第一个参数是this
                            if (Modifier.isStatic(method.getModifiers())) {
                                parameterNames[index] = name;
                            }
                            else if (index > 0) {
                                parameterNames[index - 1] = name;
                            }
                        }
                    };

                }
            }, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parameterNames;
    }

    /**
     * 自定义参数绑定
     *
     * */
    public static Object[] toObjectArrys(HttpServletRequest request, HttpServletResponse repsonse, Handler handler, Map<String,Object> params,Method method,Context context,Object[] objects){
        //参数的类型
        Type[] argumentTypes = paramTpyes.get(method);
        String[] paramName = handler.getParamsName();
        //Object[] objects = new Object[paramTpyes.size()]; //这里生命有点问题
        for(int i = 0; i < argumentTypes.length ; i++){
            Object o = null;
            String param = paramName[i];
            System.out.println("no."+i);
            System.out.println(argumentTypes[i].getInternalName());
            if(isConst(argumentTypes[i].getInternalName())){
                o = params.get(param);
            }else{
                o = getHttpRequest(argumentTypes[i].getInternalName(),context ) ;
            }
            if(o != null) {
                objects[i] = o;
                System.out.println(objects[i]);
            }else{
                return null;
            }
        }
        System.out.println(objects[0]+":"+objects[1]);
        return objects;
    }
    private static boolean isConst(String paramName){
        if(paramName.equals("java/lang/String") || paramName.equals("java/lang/Integer") || paramName.equals("java/lang/List") || paramName.equals("java/lang/Map")){
            return true;
        }else{
            return false;
        }
    }
    private static Object getHttpRequest(String paramType,Context context){
        if(paramType.equals("javax/servlet/http/HttpServletRequest")){
            return context.getRequest();
        }else if(paramType.equals("javax/servlet/http/HttpServletResponse")){
            return context.getResponse();
        }else if(paramType.equals("javax/servlet/http/HttpSession")){
            return context.getRequest().getSession();
        }else{
            return null;
        }
    }
}
