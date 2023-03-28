package com.joizhang.naiverpc.netty.serialize.kryo.utils;

import com.esotericsoftware.kryo.Kryo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class KryoUtils {

    private static final AbstractKryoFactory KRYO_FACTORY = new ThreadLocalKryoFactory();

    public static Kryo get() {
        return KRYO_FACTORY.getKryo();
    }

    public static void release(Kryo kryo) {
        KRYO_FACTORY.returnKryo(kryo);
    }

    public static void register(Class<?> clazz) {
        KRYO_FACTORY.registerClass(clazz);
    }

    public static void setRegistrationRequired(boolean registrationRequired) {
        KRYO_FACTORY.setRegistrationRequired(registrationRequired);
    }

}
