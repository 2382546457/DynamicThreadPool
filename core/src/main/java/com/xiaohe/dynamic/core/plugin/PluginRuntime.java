package com.xiaohe.dynamic.core.plugin;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;


/**
 * 插件的执行信息
 */
@Getter
@RequiredArgsConstructor
public class PluginRuntime {

    private final String pluginId;

    private final List<Info> infoList = new ArrayList<>();

    public PluginRuntime addInfo(String key, Object value) {
        infoList.add(new Info(key, value));
        return this;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Info {

        /**
         * Name
         */
        private final String key;

        /**
         * Value
         */
        private final Object value;
    }
}
