package com.student.util;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DynamicCompilation {

    public static void main(String[] args) {
        String className = "DynamicClass";
        String code = "public class DynamicClass { public void printMessage() {System.out.println(\"Hello, dynamic compilation!\");}}";

        // 生成Java源文件
        String fileName = className + ".java";
        File sourceFile = new File(fileName);
        try {
            FileWriter writer = new FileWriter(sourceFile);
            writer.write(code);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 动态编译Java源文件为字节码
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int compilationResult = compiler.run(null, null, null, sourceFile.getPath());
        if (compilationResult == 0) {
            System.out.println("Compilation success!");

            // 加载动态编译生成的类并使用它
            try {
                Class<?> dynamicClass = Class.forName(className);
                Object instance = dynamicClass.getDeclaredConstructor().newInstance();
                dynamicClass.getMethod("printMessage").invoke(instance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Compilation failed!");
        }
    }

    private static void BeanRegister(){

    }
}

