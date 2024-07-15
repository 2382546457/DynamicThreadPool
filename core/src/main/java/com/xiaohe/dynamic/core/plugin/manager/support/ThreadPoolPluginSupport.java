package com.xiaohe.dynamic.core.plugin.manager.support;

import com.xiaohe.dynamic.core.plugin.*;
import com.xiaohe.dynamic.core.plugin.manager.ThreadPoolPluginManager;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池插件支持器，内含线程池id、线程池、线程池插件管理器
 */
public interface ThreadPoolPluginSupport extends ThreadPoolPluginManager {

    ThreadPoolExecutor getThreadExecutor();

    ThreadPoolPluginManager getThreadPoolPluginManager();

    String getThreadPoolId();


    @Override
    default void clear() {
        getThreadPoolPluginManager().clear();
    }


    @Override
    default void register(ThreadPoolPlugin plugin) {
        getThreadPoolPluginManager().register(plugin);
    }

    @Override
    default boolean tryRegister(ThreadPoolPlugin plugin) {
        return getThreadPoolPluginManager().tryRegister(plugin);
    }


    @Override
    default boolean isRegistered(String pluginId) {
        return getThreadPoolPluginManager().isRegistered(pluginId);
    }

    @Override
    default void unregister(String pluginId) {
        getThreadPoolPluginManager().unregister(pluginId);
    }

    @Override
    default Collection<ThreadPoolPlugin> getAllPlugins() {
        return getThreadPoolPluginManager().getAllPlugins();
    }

    @Override
    default <A extends ThreadPoolPlugin> Optional<A> getPlugin(String pluginId) {
        return getThreadPoolPluginManager().getPlugin(pluginId);
    }

    @Override
    default Collection<ExecuteAwarePlugin> getExecuteAwarePluginList() {
        return getThreadPoolPluginManager().getExecuteAwarePluginList();
    }

    @Override
    default Collection<RejectedAwarePlugin> getRejectedAwarePluginList() {
        return getThreadPoolPluginManager().getRejectedAwarePluginList();
    }

    @Override
    default Collection<ShutdownAwarePlugin> getShutdownAwarePluginList() {
        return getThreadPoolPluginManager().getShutdownAwarePluginList();
    }

    @Override
    default Collection<TaskAwarePlugin> getTaskAwarePluginList() {
        return getThreadPoolPluginManager().getTaskAwarePluginList();
    }

}
