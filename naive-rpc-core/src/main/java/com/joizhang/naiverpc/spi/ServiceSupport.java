package com.joizhang.naiverpc.spi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.StreamSupport;

/**
 * Loading rpc extensions
 * @param <T>
 */
public class ServiceSupport<T> {

    private static final ConcurrentMap<Class<?>, ServiceSupport<?>> SERVICE_LOADERS = new ConcurrentHashMap<>(16);

    private final ConcurrentMap<String, T> cachedInstances = new ConcurrentHashMap<>();

    private final Class<?> type;

    private ServiceSupport(Class<?> type) {
        this.type = type;
    }

    private static <T> boolean withExtensionAnnotation(Class<T> type) {
        return type.isAnnotationPresent(SPI.class);
    }

    @SuppressWarnings("unchecked")
    public static <T> ServiceSupport<T> getServiceSupport(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Service type == null");
        }
        if (!type.isInterface()) {
            throw new IllegalArgumentException("Service type (" + type + ") is not an interface!");
        }
        if (!withExtensionAnnotation(type)) {
            throw new IllegalArgumentException("Service type (" + type + ") is not an extension, " +
                    "because it is NOT annotated with @" + SPI.class.getSimpleName() + "!");
        }

        ServiceSupport<T> loader = (ServiceSupport<T>) SERVICE_LOADERS.get(type);
        if (loader == null) {
            SERVICE_LOADERS.putIfAbsent(type, new ServiceSupport<T>(type));
            loader = (ServiceSupport<T>) SERVICE_LOADERS.get(type);
        }
        return loader;
    }

    public T getService(String name) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException("Service name == null");
        }
        T service = cachedInstances.get(name);
        if (Objects.isNull(service)) {
            synchronized (cachedInstances) {
                service = cachedInstances.get(name);
                if (Objects.isNull(service)) {
                    cacheAll();
                    service = cachedInstances.get(name);
                }
            }
        }
        return service;
    }

    public Collection<T> getAllService() {
        if (cachedInstances.isEmpty()) {
            cacheAll();
        }
        Collection<T> collection = new ArrayList<>(cachedInstances.size());
        cachedInstances.forEach((serviceName, instance) -> collection.add(instance));
        return collection;
    }

    @SuppressWarnings("unchecked")
    public void cacheAll() {
        StreamSupport
                .stream(ServiceLoader.load(type).spliterator(), false)
                .forEach((instance) -> cachedInstances.putIfAbsent(instance.getClass().getCanonicalName(), (T) instance));
    }

}
