package com.xiaohe.dynamic.core.plugin.impl;


import com.xiaohe.dynamic.core.plugin.ExecuteAwarePlugin;
import com.xiaohe.dynamic.core.toolkit.SystemClock;

/**
 * mark任务的开始和结束时间，交给用户处理
 */
public abstract class AbstractTaskTimerPlugin implements ExecuteAwarePlugin {

    /**
     * 该任务执行开始时间
     */
    private final ThreadLocal<Long> startTimes = new ThreadLocal<>();

    protected abstract void processTaskTime(long taskExecuteTime);

    protected long currentTime() {
        return SystemClock.now();
    }

    @Override
    public void beforeExecute(Thread thread, Runnable runnable) {
        startTimes.set(currentTime());
    }

    @Override
    public void afterExecute(Thread thread, Throwable throwable) {
        try {
            Long start = startTimes.get();
            if (start != null) {
                processTaskTime(currentTime() - start);
            }
        } finally {
            startTimes.remove();
        }
    }
}
