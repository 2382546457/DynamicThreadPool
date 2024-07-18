package com.xiaohe.dynamic.core.plugin.manager.global;

import com.xiaohe.dynamic.core.plugin.ThreadPoolPlugin;
import com.xiaohe.dynamic.core.plugin.manager.register.ThreadPoolPluginRegistrar;
import com.xiaohe.dynamic.core.plugin.manager.support.ThreadPoolPluginSupport;
import lombok.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultGlobalThreadPoolPluginManager implements GlobalThreadPoolPluginManager {

    /**
     * 所有启用的插件
     */
    private final Map<String, ThreadPoolPlugin> enableThreadPoolPlugins = new ConcurrentHashMap<>(8);

    /**
     * 所有启用的插件注册器
     */
    private final Map<String, ThreadPoolPluginRegistrar> enableThreadPoolPluginRegistrars = new ConcurrentHashMap<>(8);


    private final Map<String, ThreadPoolPluginSupport> managedThreadPoolPluginSupports = new ConcurrentHashMap<>(32);


    @Override
    public void doRegister(ThreadPoolPluginSupport support) {
        enableThreadPoolPluginRegistrars.values().forEach(registrar -> registrar.doRegister(support));
        enableThreadPoolPlugins.values().forEach(support::tryRegister);
    }

    @Override
    public boolean registerThreadPoolPluginSupport(@NonNull ThreadPoolPluginSupport support) {
        if (!managedThreadPoolPluginSupports.containsKey(support.getThreadPoolId())) {
            enableThreadPoolPluginRegistrars.values().forEach(registrar -> registrar.doRegister(support));
            enableThreadPoolPlugins.values().forEach(support::tryRegister);
            managedThreadPoolPluginSupports.put(support.getThreadPoolId(), support);
            return true;
        }
        return false;
    }
    @Override
    public ThreadPoolPluginSupport cancelManagement(String threadPoolId) {
        return managedThreadPoolPluginSupports.remove(threadPoolId);
    }

    @Nullable
    @Override
    public ThreadPoolPluginSupport getManagedThreadPoolPluginSupport(String threadPoolId) {
        return managedThreadPoolPluginSupports.get(threadPoolId);
    }

    @Override
    public Collection<ThreadPoolPluginSupport> getAllManagedThreadPoolPluginSupports() {
        return managedThreadPoolPluginSupports.values();
    }

    @Override
    public boolean enableThreadPoolPlugin(@NonNull ThreadPoolPlugin plugin) {
        if (Objects.isNull(enableThreadPoolPlugins.put(plugin.getID(), plugin))) {
            managedThreadPoolPluginSupports.values().forEach(support -> support.register(plugin));
            return true;
        }
        return false;
    }

    @Override
    public Collection<ThreadPoolPlugin> getAllEnableThreadPoolPlugins() {
        return enableThreadPoolPlugins.values();
    }

    @Override
    public ThreadPoolPlugin disableThreadPoolPlugin(String pluginId) {
        ThreadPoolPlugin removed = enableThreadPoolPlugins.remove(pluginId);
        if (Objects.nonNull(removed)) {
            managedThreadPoolPluginSupports.values().forEach(support -> support.unregister(pluginId));
        }

        return removed;
    }

    @Override
    public boolean enableThreadPoolPluginRegistrar(@NonNull ThreadPoolPluginRegistrar registrar) {
        if (Objects.isNull(enableThreadPoolPluginRegistrars.put(registrar.getId(), registrar))) {
            managedThreadPoolPluginSupports.values().forEach(registrar::doRegister);
            return true;
        }
        return false;
    }


    @Override
    public Collection<ThreadPoolPluginRegistrar> getAllEnableThreadPoolPluginRegistrar() {
        return enableThreadPoolPluginRegistrars.values();
    }


    @Override
    public ThreadPoolPluginRegistrar disableThreadPoolPluginRegistrar(String registrarId) {
        return enableThreadPoolPluginRegistrars.remove(registrarId);
    }
}
