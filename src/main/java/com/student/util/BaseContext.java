package com.student.util;

public class BaseContext {

    public static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(String name) {
        threadLocal.set(name);
    }

    public static String getCurrentId() {
        return threadLocal.get();
    }

    public static void removeCurrentId() {
        threadLocal.remove();
    }

}
