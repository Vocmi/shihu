package com.shihu.util;

public class BaseContext {

    public static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void setCurrentToken(String token) {
        threadLocal.set(token);
    }

    public static String getCurrentToken() {
        return threadLocal.get();
    }

    public static void removeCurrentToken() {
        threadLocal.remove();
    }

}
