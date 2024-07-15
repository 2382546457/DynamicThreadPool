package com.xiaohe.dynamic.core.plugin;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;

public interface TaskAwarePlugin {

    /**
     * RunnableFuture创建时调用(指定返回值)
     * @param executor
     * @param runnable
     * @param value 返回值
     * @return
     * @param <V>
     */
    default <V> Runnable beforeTaskCreate(ThreadPoolExecutor executor, Runnable runnable, V value) {
        return runnable;
    }

    /**
     * RunnableFuture创建时调用(不指定返回值)
     * @param executor
     * @param future
     * @return
     * @param <V>
     */
    default <V> Callable<V> beforeTaskCreate(ThreadPoolExecutor executor, Callable<V> future) {
        return future;
    }

    default Runnable beforeTaskExecute(Runnable runnable) {
        return runnable;
    }
}
