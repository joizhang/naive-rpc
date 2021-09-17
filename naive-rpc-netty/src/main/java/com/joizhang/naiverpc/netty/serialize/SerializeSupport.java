package com.joizhang.naiverpc.netty.serialize;

import com.joizhang.naiverpc.serialize.Serializer;
import com.joizhang.naiverpc.spi.ServiceSupport;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SerializeSupport {

    private static final ServiceSupport<Serializer> SERIALIZER_SERVICE_SUPPORT = ServiceSupport.getServiceSupport(Serializer.class);

    /**
     * 序列化对象类型 : 序列化实现
     */
    private static final Map<Class<?>, Serializer> serializerMap = new HashMap<>();

    /**
     * 序列化实现类型 : 序列化对象类型
     */
    private static final Map<Byte, Class<?>> typeMap = new HashMap<>();

    static {
        for (Serializer serializer : SERIALIZER_SERVICE_SUPPORT.getAllService()) {
            registerType(serializer.getContentTypeId(), serializer.getClass(), serializer);
            log.info("Found serializer, class: {}, type: {}.",
                    serializer.getClass().getCanonicalName(),
                    serializer.getContentTypeId());
        }
    }

    private static byte parseEntryType(byte[] buffer) {
        return buffer[0];
    }

    private static <E> void registerType(byte type, Class<E> eClass, Serializer serializer) {
        serializerMap.put(eClass, serializer);
        typeMap.put(type, eClass);
    }

    @SuppressWarnings("unchecked")
    private static <E> E parse(byte[] buffer, int offset, int length, Class<E> eClass) {
//        Object entry = serializerMap.get(eClass).parse(buffer, offset, length);
        Object entry = new Object();
        if (eClass.isAssignableFrom(entry.getClass())) {
            return (E) entry;
        } else {
            throw new SerializeException("Type mismatch!");
        }
    }

    public static <E> E parse(byte[] buffer) {
        return parse(buffer, 0, buffer.length);
    }

    @SuppressWarnings("unchecked")
    private static <E> E parse(byte[] buffer, int offset, int length) {
        byte type = parseEntryType(buffer);
        Class<E> eClass = (Class<E>) typeMap.get(type);
        if (null == eClass) {
            throw new SerializeException(String.format("Unknown entry type: %d!", type));
        } else {
            return parse(buffer, offset + 1, length - 1, eClass);
        }

    }

    @SuppressWarnings("unchecked")
    public static <E> byte[] serialize(E entry) {
        Serializer serializer = serializerMap.get(entry.getClass());
        if (serializer == null) {
            throw new SerializeException(String.format("Unknown entry class type: %s", entry.getClass().toString()));
        }
//        byte[] bytes = new byte[serializer.size(entry) + 1];
        byte[] bytes = new byte[1];
        bytes[0] = serializer.getContentTypeId();
//        serializer.serialize(entry, bytes, 1, bytes.length - 1);
        return bytes;
    }

}
