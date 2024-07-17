package com.xiaohe.dynamic.core.plugin.manager.register;

import com.xiaohe.dynamic.core.plugin.manager.support.ThreadPoolPluginSupport;


/**
 * 线程池插件注册器
 * 不是注册线程池插件的，而是将插件注册到线程池中的
 */
public interface ThreadPoolPluginRegistrar {

    default String getId() {
        return this.getClass().getSimpleName();
    }


    void doRegister(ThreadPoolPluginSupport support);
}
