package com.student.util;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class DynamicCompilationExample {

    public static void main(String[] args) throws Exception {
        // 源代码
        String sourceCode = "public class HelloWorld { "
                + "    public static void main(String[] args) { "
                + "        System.out.println(\"Hello, dynamic compilation!\");"
                + "    } "
                + "} ";

        // 创建编译器对象
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        // 创建内存中的源代码文件
        File sourceFile = new File("HelloWorld.java");
        java.nio.file.Files.write(sourceFile.toPath(), sourceCode.getBytes());

        // 执行编译，将源代码编译为字节码文件
        int compilationResult = compiler.run(null, null, null, sourceFile.getPath());

        if (compilationResult == 0) {
            // 创建URLClassLoader用于加载编译后的类
            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{sourceFile.getParentFile().toURI().toURL()});

            // 加载动态编译后的类
            Class<?> compiledClass = Class.forName("HelloWorld", true, classLoader);

            // 调用动态编译后的类的main方法
            compiledClass.getMethod("main", String[].class).invoke(null, (Object) null);
        } else {
            System.out.println("Compilation Failed");
        }
    }
}
