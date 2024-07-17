package com.xiaohe.dynamic.core.plugin.manager.register;

import com.xiaohe.dynamic.core.plugin.impl.TaskRejectCountRecordPlugin;
import com.xiaohe.dynamic.core.plugin.impl.TaskTimeRecordPlugin;
import com.xiaohe.dynamic.core.plugin.manager.support.ThreadPoolPluginSupport;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


/**
 * 默认的插件注册器，向指定的线程池中注册一些默认的线程池插件
 */
@NoArgsConstructor
@AllArgsConstructor
public class DefaultThreadPoolPluginRegister implements ThreadPoolPluginRegistrar {

    /**
     * 执行超时时间
     */
    private long executeTimeOut;

    /**
     *
     */
    private long awaitTerminationMills;


    @Override
    public void doRegister(ThreadPoolPluginSupport support) {
        support.register(new TaskRejectCountRecordPlugin());
        support.register(new TaskTimeRecordPlugin());
    }
}
