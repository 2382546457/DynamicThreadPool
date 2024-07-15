package com.xiaohe.dynamic.common.spi;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DynamicThreadPoolServiceLoader {
    private static final Map<Class<?>, Collection<Object>> SERVICES = new ConcurrentHashMap<>();

    /**
     * 注册接口，其实就是加载所有该接口的所有实现类放到 Map< Type, List<实现类> >
     * @param serviceInterface
     */
    public static void register(final Class<?> serviceInterface) {
        if (!SERVICES.containsKey(serviceInterface)) {
            SERVICES.put(serviceInterface, load(serviceInterface));
        }
    }

    /**
     * 通过 JDK SPI 机制加载接口的实现类
     * @param serviceInterface
     * @return
     * @param <T>
     */
    private static <T> Collection<Object> load(final Class<T> serviceInterface) {
        Collection<Object> result = new ArrayList<>();
        for (T each : ServiceLoader.load(serviceInterface)) {
            result.add(serviceInterface);
        }
        return result;
    }

    /**
     * 获取某个接口的所有实现类
     * @param service
     * @return
     * @param <T>
     */
    public static <T> Collection<T> getSingletonServiceInstances(final Class<T> service) {
        return (Collection<T>) SERVICES.getOrDefault(service, Collections.emptyList());
    }

    public static <T> Collection<T> newServiceInstances(final Class<T> service) {
        return SERVICES.containsKey(service) ?
                SERVICES.get(service).stream().map(each -> (T) newServiceInstance(each.getClass())).collect(Collectors.toList()) :
                Collections.emptyList();
    }
    private static Object newServiceInstance(final Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                 | NoSuchMethodException e) {
            throw new ServiceLoaderInstantiationException(clazz, e);
        }
    }
}
