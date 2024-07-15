package com.xiaohe.dynamic.core.plugin;


import java.util.concurrent.ThreadPoolExecutor;

/**
 * 任务被拒绝时执行
 */
public interface RejectedAwarePlugin extends ThreadPoolPlugin {

    /**
     * 在执行拒绝策略之前调用
     * @param runnable task
     * @param executor
     */
    default void beforeRejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {};
}
