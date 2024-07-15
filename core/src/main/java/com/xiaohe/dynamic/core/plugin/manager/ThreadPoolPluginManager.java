package com.xiaohe.dynamic.core.plugin.manager;

import com.xiaohe.dynamic.core.plugin.*;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 线程池插件管理器
 */
public interface ThreadPoolPluginManager {

    /**
     * 返回一个空的线程池插件管理器
     * @return
     */
    static ThreadPoolPluginManager empty() {
        return EmptyThreadPoolPluginManager.INSTANCE;
    }

    void clear();

    Collection<ThreadPoolPlugin> getAllPlugins();

    void register(ThreadPoolPlugin plugin);

    boolean tryRegister(ThreadPoolPlugin plugin);

    boolean isRegistered(String pluginId);

    void unregister(String pluginId);

    <A extends ThreadPoolPlugin> Optional<A> getPlugin(String pluginId);


    Collection<ExecuteAwarePlugin> getExecuteAwarePluginList();


    Collection<RejectedAwarePlugin> getRejectedAwarePluginList();


    Collection<ShutdownAwarePlugin> getShutdownAwarePluginList();

    Collection<TaskAwarePlugin> getTaskAwarePluginList();


    default <A extends ThreadPoolPlugin> Optional<A> getPluginOfType(String pluginId, Class<A> pluginType) {
        return getPlugin(pluginId)
                .filter(pluginType::isInstance)
                .map(pluginType::cast);
    }


    default <A extends ThreadPoolPlugin> Collection<A> getAllPluginsOfType(Class<A> pluginType) {
        return getAllPlugins().stream()
                .filter(pluginType::isInstance)
                .map(pluginType::cast)
                .collect(Collectors.toList());
    }


    default Collection<PluginRuntime> getAllPluginRuntimes() {
        return getAllPlugins().stream()
                .map(ThreadPoolPlugin::getPluginRuntime)
                .collect(Collectors.toList());
    }


    default Optional<PluginRuntime> getRuntime(String pluginId) {
        return getPlugin(pluginId)
                .map(ThreadPoolPlugin::getPluginRuntime);
    }
}
