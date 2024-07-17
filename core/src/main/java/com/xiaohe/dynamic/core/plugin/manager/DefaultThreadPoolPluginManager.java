package com.xiaohe.dynamic.core.plugin.manager;

import com.xiaohe.dynamic.core.plugin.*;
import lombok.NonNull;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 默认的线程池插件管理器
 * 每一个线程池都持有一个管理器
 */
public class DefaultThreadPoolPluginManager implements ThreadPoolPluginManager {

    private final ReadWriteLock instanceLock = new ReentrantReadWriteLock();

    /**
     * 所有已注册的插件
     */
    private final Map<String, ThreadPoolPlugin> registeredPlugins = new ConcurrentHashMap<>();

    private final List<TaskAwarePlugin> taskAwarePluginList = new CopyOnWriteArrayList<>();

    private final List<ExecuteAwarePlugin> executeAwarePluginList = new CopyOnWriteArrayList<>();

    private final List<RejectedAwarePlugin> rejectedAwarePluginList = new CopyOnWriteArrayList<>();

    private final List<ShutdownAwarePlugin> shutdownAwarePluginList = new CopyOnWriteArrayList<>();

    @Override
    public void clear() {
        Lock writeLock = instanceLock.writeLock();
        writeLock.lock();
        try {
            Collection<ThreadPoolPlugin> plugins = registeredPlugins.values();
            registeredPlugins.clear();
            taskAwarePluginList.clear();
            executeAwarePluginList.clear();
            rejectedAwarePluginList.clear();
            shutdownAwarePluginList.clear();
            plugins.forEach(ThreadPoolPlugin::stop);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void register(@NonNull ThreadPoolPlugin plugin) {
        Lock writeLock = instanceLock.writeLock();
        writeLock.lock();
        try {
            String id = plugin.getID();
            Assert.isTrue(!isRegistered(id), "该插件已注册, id:" + id);
            registeredPlugins.put(id, plugin);
            if (plugin instanceof TaskAwarePlugin) {
                taskAwarePluginList.add((TaskAwarePlugin) plugin);
            }
            if (plugin instanceof ExecuteAwarePlugin) {
                executeAwarePluginList.add((ExecuteAwarePlugin) plugin);
            }
            if (plugin instanceof RejectedAwarePlugin) {
                rejectedAwarePluginList.add((RejectedAwarePlugin) plugin);
            }
            if (plugin instanceof ShutdownAwarePlugin) {
                shutdownAwarePluginList.add((ShutdownAwarePlugin) plugin);
            }
            plugin.start();
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean isRegistered(String pluginId) {
        Lock readLock = instanceLock.readLock();
        readLock.lock();
        try {
            return registeredPlugins.containsKey(pluginId);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean tryRegister(ThreadPoolPlugin plugin) {
        Lock writeLock = instanceLock.writeLock();
        writeLock.lock();
        try {
            if (registeredPlugins.containsKey(plugin.getID())) {
                return false;
            }
            register(plugin);
            return true;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void unregister(String pluginId) {
        Lock writeLock = instanceLock.writeLock();
        writeLock.lock();
        try {
            Optional.ofNullable(pluginId)
                    .map(registeredPlugins::remove)
                    .ifPresent(plugin -> {
                        if (plugin instanceof TaskAwarePlugin) {
                            taskAwarePluginList.remove(plugin);
                        }
                        if (plugin instanceof ExecuteAwarePlugin) {
                            executeAwarePluginList.remove(plugin);
                        }
                        if (plugin instanceof RejectedAwarePlugin) {
                            rejectedAwarePluginList.remove(plugin);
                        }
                        if (plugin instanceof ShutdownAwarePlugin) {
                            shutdownAwarePluginList.remove(plugin);
                        }
                        plugin.stop();
                    });
        } finally {
            writeLock.unlock();
        }
    }
    @Override
    public Collection<ThreadPoolPlugin> getAllPlugins() {
        Lock readLock = instanceLock.readLock();
        readLock.lock();
        try {
            return registeredPlugins.values();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <A extends ThreadPoolPlugin> Optional<A> getPlugin(String pluginId) {
        Lock readLock = instanceLock.readLock();
        readLock.lock();
        try {
            return (Optional<A>) Optional.ofNullable(registeredPlugins.get(pluginId));
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Collection<ExecuteAwarePlugin> getExecuteAwarePluginList() {
        Lock readLock = instanceLock.readLock();
        readLock.lock();
        try {
            return executeAwarePluginList;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Collection<RejectedAwarePlugin> getRejectedAwarePluginList() {
        Lock readLock = instanceLock.readLock();
        readLock.lock();
        try {
            return rejectedAwarePluginList;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Collection<ShutdownAwarePlugin> getShutdownAwarePluginList() {
        Lock readLock = instanceLock.readLock();
        readLock.lock();
        try {
            return shutdownAwarePluginList;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Collection<TaskAwarePlugin> getTaskAwarePluginList() {
        Lock readLock = instanceLock.readLock();
        readLock.lock();
        try {
            return taskAwarePluginList;
        } finally {
            readLock.unlock();
        }
    }
}
