package com.xiaohe.dynamic.core.plugin;

/**
 * 动态线程池插件
 */
public interface ThreadPoolPlugin {

    /**
     * 获取插件id
     * @return
     */
    String getID();

    /**
     * 插件注册到插件管理器中时触发
     */
    default void start() {}

    /**
     * 插件取消注册时触发
     */
    default void stop() {}

    default PluginRuntime getPluginRuntime() {
        return new PluginRuntime(getID());
    }
}
