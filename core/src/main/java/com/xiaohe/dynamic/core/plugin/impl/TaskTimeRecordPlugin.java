package com.xiaohe.dynamic.core.plugin.impl;

import com.xiaohe.dynamic.core.plugin.PluginRuntime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * 记录任务的执行时间
 */
@RequiredArgsConstructor
public class TaskTimeRecordPlugin extends AbstractTaskTimerPlugin {

    public static final String PLUGIN_NAME = "task-time-record-plugin";

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * 任务执行总时长
     */
    private long totalTaskTimeMillis = 0L;

    /**
     * 任务执行最大时长
     */
    private long maxTaskTimeMillis = -1L;

    /**
     * 任务执行最小时长
     */
    private long minTaskTimeMillis = -1L;

    /**
     * 已执行的任务数量
     */
    private long taskCount = 0L;


    /**
     * 统计一下任务的最大、最小、总计执行时间以及执行次数
     * @param taskExecuteTime
     */
    @Override
    protected void processTaskTime(long taskExecuteTime) {
        Lock writeLock = lock.writeLock();
        writeLock.lock();
        try {
            if (taskCount == 0) {
                maxTaskTimeMillis = taskExecuteTime;
                minTaskTimeMillis = taskExecuteTime;
            } else {
                maxTaskTimeMillis = Math.max(maxTaskTimeMillis, taskExecuteTime);
                minTaskTimeMillis = Math.min(minTaskTimeMillis, taskExecuteTime);
            }
            taskCount += 1;
            totalTaskTimeMillis += taskExecuteTime;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public String getID() {
        return PLUGIN_NAME;
    }

    @Override
    public PluginRuntime getPluginRuntime() {
        Summary summarize = summarize();
        return new PluginRuntime(getID())
                .addInfo("taskCount", summarize.getTaskCount() + "ms")
                .addInfo("maxTaskTimeMillis", summarize.getMaxTaskTimeMillis() + "ms")
                .addInfo("minTaskTimeMillis", summarize.getMinTaskTimeMillis() + "ms")
                .addInfo("totalTaskTimeMillis", summarize.getTotalTaskTimeMillis() + "ms")
                .addInfo("avgTaskTime", summarize.getAvgTaskTimeMillis() + "ms");

    }

    /**
     * 得到数据快照
     * @return
     */
    public Summary summarize() {
        Lock readLock = lock.readLock();
        Summary statistic;
        readLock.lock();
        try {
            statistic = new Summary(
                    this.totalTaskTimeMillis,
                    this.maxTaskTimeMillis,
                    this.minTaskTimeMillis,
                    this.taskCount
            );
        } finally {
            readLock.unlock();
        }
        return statistic;
    }

    /**
     * 可以理解为快照
     */
    @Getter
    @RequiredArgsConstructor
    public static class Summary {

        private final long totalTaskTimeMillis;


        private final long maxTaskTimeMillis;


        private final long minTaskTimeMillis;


        private final long taskCount;

        public long getAvgTaskTimeMillis() {
            long totalTaskCount = getTaskCount();
            return totalTaskCount > 0L ? getTotalTaskTimeMillis() / totalTaskCount : -1;
        }
    }
}
