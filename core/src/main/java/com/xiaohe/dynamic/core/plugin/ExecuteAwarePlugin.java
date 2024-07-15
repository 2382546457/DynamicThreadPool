package com.xiaohe.dynamic.core.plugin;


/**
 * 任务执行增强插件
 */
public interface ExecuteAwarePlugin extends ThreadPoolPlugin {

    /**
     * 任务执行前调用
     * @param thread
     * @param runnable
     */
    default void beforeExecute(Thread thread, Runnable runnable) {}

    /**
     * 任务执行后或异常后调用
     * @param thread
     * @param throwable
     */
    default void afterExecute(Thread thread, Throwable throwable) {}
}
