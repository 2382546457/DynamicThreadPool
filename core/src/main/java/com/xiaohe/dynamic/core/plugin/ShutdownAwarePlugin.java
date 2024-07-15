package com.xiaohe.dynamic.core.plugin;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

public interface ShutdownAwarePlugin extends ThreadPoolPlugin {

    /**
     * 线程池结束前调用
     */
    default void beforeShutdown(ThreadPoolExecutor executor) {
    }

    /**
     * 线程池结束后调用
     */
    default void afterShutdown(ThreadPoolExecutor executor, List<Runnable> remainingTasks) {
    }

    /**
     * 线程池终止后调用
     */
    default void afterTerminated(ExtensibleThreadPoolExecutor executor) {
    }
}