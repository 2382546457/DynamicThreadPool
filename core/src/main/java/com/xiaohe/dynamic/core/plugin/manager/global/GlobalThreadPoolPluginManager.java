package com.xiaohe.dynamic.core.plugin.manager.global;

import com.xiaohe.dynamic.core.plugin.ThreadPoolPlugin;
import com.xiaohe.dynamic.core.plugin.manager.register.ThreadPoolPluginRegistrar;
import com.xiaohe.dynamic.core.plugin.manager.support.ThreadPoolPluginSupport;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * 对于全部的线程池插件管理器进行管理。相当于：
 * 每一个线程池拥有一个该线程池的插件管理器 ThreadPoolPluginManager
 * 这里会对所有 ThreadPoolPluginManager 进行统一管理
 */
public interface GlobalThreadPoolPluginManager extends ThreadPoolPluginRegistrar {

    @Override
    void doRegister(ThreadPoolPluginSupport support);


    boolean registerThreadPoolPluginSupport(ThreadPoolPluginSupport support);

    ThreadPoolPluginSupport cancelManagement(String threadPoolId);


    ThreadPoolPluginSupport getManagedThreadPoolPluginSupport(String threadPoolId);


    Collection<ThreadPoolPluginSupport> getAllManagedThreadPoolPluginSupports();

    boolean enableThreadPoolPlugin(ThreadPoolPlugin plugin);

    Collection<ThreadPoolPlugin> getAllEnableThreadPoolPlugins();

    ThreadPoolPlugin disableThreadPoolPlugin(String pluginId);

    /**
     * 从所有插件管理器手中得到插件
     * @return
     */
    default Collection<ThreadPoolPlugin> getAllPluginsFromManagers() {
        return getAllManagedThreadPoolPluginSupports().stream()
                .map(ThreadPoolPluginSupport::getAllPlugins)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    default <A extends ThreadPoolPlugin> Collection<A> getPluginsOfTypeFromManagers(Class<A> pluginType) {
        return getAllPluginsFromManagers().stream()
                .filter(pluginType::isInstance)
                .map(pluginType::cast)
                .collect(Collectors.toList());
    }

    default Collection<ThreadPoolPlugin> getPluginsFromManagers(String pluginId) {
        return getAllManagedThreadPoolPluginSupports().stream()
                .map(manager -> manager.getPlugin(pluginId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    default void unregisterForAllManagers(String pluginId) {
        getAllManagedThreadPoolPluginSupports().forEach(s -> s.unregister(pluginId));
    }

    boolean enableThreadPoolPluginRegistrar(ThreadPoolPluginRegistrar registrar);

    Collection<ThreadPoolPluginRegistrar> getAllEnableThreadPoolPluginRegistrar();

    ThreadPoolPluginRegistrar disableThreadPoolPluginRegistrar(String registrarId);
}
