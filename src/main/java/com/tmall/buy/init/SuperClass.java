package com.tmall.buy.init;

import java.lang.reflect.Method;

/**
 * @author tianmai.fh
 * @date 2014-05-20 19:40
 */
public class SuperClass {
    static Method[] methods;
    static{
         methods = getClazz().getDeclaredMethods();
    }
    public static void print(){
        for(Method method : methods){
            System.out.println(method.getName());
        }
    }

    public static Class<?> getClazz(){
         return SuperClass.class;
    }
    public void process(){

    }

}
