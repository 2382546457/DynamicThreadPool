package com.xiaohe.dynamic.core.plugin.manager;

import com.xiaohe.dynamic.core.plugin.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmptyThreadPoolPluginManager implements ThreadPoolPluginManager {


    public static final EmptyThreadPoolPluginManager INSTANCE = new EmptyThreadPoolPluginManager();


    @Override
    public void clear() {
    }


    @Override
    public Collection<ThreadPoolPlugin> getAllPlugins() {
        return Collections.emptyList();
    }


    @Override
    public void register(ThreadPoolPlugin plugin) {
    }


    @Override
    public boolean tryRegister(ThreadPoolPlugin plugin) {
        return false;
    }


    @Override
    public boolean isRegistered(String pluginId) {
        return false;
    }

    @Override
    public void unregister(String pluginId) {
    }


    @Override
    public <A extends ThreadPoolPlugin> Optional<A> getPlugin(String pluginId) {
        return Optional.empty();
    }

    @Override
    public Collection<ExecuteAwarePlugin> getExecuteAwarePluginList() {
        return Collections.emptyList();
    }

    @Override
    public Collection<RejectedAwarePlugin> getRejectedAwarePluginList() {
        return Collections.emptyList();
    }

    @Override
    public Collection<ShutdownAwarePlugin> getShutdownAwarePluginList() {
        return Collections.emptyList();
    }

    @Override
    public Collection<TaskAwarePlugin> getTaskAwarePluginList() {
        return Collections.emptyList();
    }
}
