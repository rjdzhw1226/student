package com.student.util;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class DynamicCompilationExample {
    public void complierAndRun(){
        try {

            System.out.println(System.getProperty("user.dir"));
            //动态编译
            JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
            String filename = "D:/DOWNLOAD/项目/student/studentDemo/student/src/main/java/com/student/pojo/a.java";
            int status = javac.run(null, null, null, "-d", System.getProperty("user.dir")+"/target/classes",filename);
            if(status!=0){
                System.out.println("没有编译成功！");
            }

            //动态执行
            Class clz = Class.forName("a");//返回与带有给定字符串名的类 或接口相关联的 Class 对象。
            Object o = clz.newInstance();
            Method method = clz.getDeclaredMethod("sayHello");//返回一个 Method 对象，该对象反映此 Class 对象所表示的类或接口的指定已声明方法
            String result= (String)method.invoke(o);//静态方法第一个参数可为null,第二个参数为实际传参
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        DynamicCompilationExample dy =new DynamicCompilationExample();
        dy.complierAndRun();
    }
}
