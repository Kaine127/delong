package com.mjs.delong.common;


/**
 * 基于ThreadLocal封装工具类，用户保存和获取当前登录用户id
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置ThreadLocal的值
     */

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    /**
     * 获取ThreadLocal的值
     */
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
