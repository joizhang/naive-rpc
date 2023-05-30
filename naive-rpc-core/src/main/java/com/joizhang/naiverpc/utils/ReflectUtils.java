package com.joizhang.naiverpc.utils;

import lombok.experimental.UtilityClass;

import java.util.Date;

@UtilityClass
public final class ReflectUtils {

    public static boolean isPrimitives(Class<?> cls) {
        while (cls.isArray()) {
            cls = cls.getComponentType();
        }
        return isPrimitive(cls);
    }

    public static boolean isPrimitive(Class<?> cls) {
        return cls.isPrimitive() ||
                cls == String.class ||
                cls == Boolean.class ||
                cls == Character.class ||
                Number.class.isAssignableFrom(cls) ||
                Date.class.isAssignableFrom(cls);
    }

}
