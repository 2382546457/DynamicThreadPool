package com.xiaohe.dynamic.core.plugin.impl;

import com.xiaohe.dynamic.core.plugin.PluginRuntime;
import com.xiaohe.dynamic.core.plugin.RejectedAwarePlugin;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;


/**
 * 统计任务拒绝数量的插件
 */
public class TaskRejectCountRecordPlugin implements RejectedAwarePlugin {

    public static final String PLUGIN_NAME = "task-reject-count-record-plugin";

    @Override
    public String getID() {
        return PLUGIN_NAME;
    }

    /**
     * 已经拒绝的任务数
     */
    private AtomicLong rejectCount = new AtomicLong(0);


    @Override
    public void beforeRejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        rejectCount.incrementAndGet();
    }


    @Override
    public PluginRuntime getPluginRuntime() {
        return new PluginRuntime(getID())
                .addInfo("rejectCount", getRejectCountNum());
    }
    public Long getRejectCountNum() {
        return rejectCount.get();
    }
}
