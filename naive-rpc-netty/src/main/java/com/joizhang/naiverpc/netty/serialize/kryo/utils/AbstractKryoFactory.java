package com.joizhang.naiverpc.netty.serialize.kryo.utils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.SerializerFactory;
import com.esotericsoftware.kryo.serializers.JavaSerializer;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractKryoFactory {

    private final Set<Class<?>> registrations = new LinkedHashSet<>();

    private static final AtomicInteger atomicInteger = new AtomicInteger(100);

    private boolean registrationRequired = true;

    private volatile boolean kryoCreated;

    /**
     * only supposed to be called at startup time
     * <p>
     * later may consider adding support for custom serializer, custom id, etc
     */
    public void registerClass(Class<?> clazz) {
        if (kryoCreated) {
            throw new IllegalStateException("Can't register class after creating kryo instance");
        }
        registrations.add(clazz);
    }

    /*
     * Class ID's reserved:
     *  -1 & -2  -> Kryo
     *   0 - 8   -> java-primitives
     *  10 - 100 -> standard-java-objects
     * 100++     -> user-space
     */
    public Kryo create() {
        if (!kryoCreated) {
            kryoCreated = true;
        }

        // Create new Kryo instance
        Kryo kryo = new Kryo();

        // Configure Kryo-instance

        // Registration of classes are required to avoid wrong class-decoding
        kryo.setRegistrationRequired(registrationRequired);

        // References are required for object-graph
//        kryo.setReferences(true);
        kryo.addDefaultSerializer(Throwable.class, new JavaSerializer());

        // Use CompatibleSerializer for back- and upward-compatibility
        SerializerFactory.CompatibleFieldSerializerFactory factory =
                new SerializerFactory.CompatibleFieldSerializerFactory();

        // FieldSerializerConfig
        factory.getConfig().setFieldsCanBeNull(true);
        factory.getConfig().setFieldsAsAccessible(true);
        factory.getConfig().setIgnoreSyntheticFields(true);
        factory.getConfig().setFixedFieldTypes(false);
        factory.getConfig().setCopyTransient(true);
        factory.getConfig().setSerializeTransient(false);
        factory.getConfig().setVariableLengthEncoding(true);
        factory.getConfig().setExtendedFieldNames(true);

        // CompatibleFieldSerializerConfig
        factory.getConfig().setReadUnknownFieldData(false);
        factory.getConfig().setChunkedEncoding(true);

        // Adding Factory as Serializer
        kryo.setDefaultSerializer(factory);

        // Register standard-java-objects
        kryo.register(HashMap.class, 10);
        kryo.register(ArrayList.class, 11);
        kryo.register(HashSet.class, 12);
        kryo.register(byte[].class, 13);
        kryo.register(char[].class, 14);
        kryo.register(short[].class, 15);
        kryo.register(int[].class, 16);
        kryo.register(long[].class, 17);
        kryo.register(float[].class, 18);
        kryo.register(double[].class, 19);
        kryo.register(boolean[].class, 20);
        kryo.register(String[].class, 21);
        kryo.register(Object[].class, 22);
        kryo.register(BigInteger.class, 23);
        kryo.register(BigDecimal.class, 24);
        kryo.register(Class.class, 25);
        kryo.register(Date.class, 26);
        kryo.register(StringBuffer.class, 27);
        kryo.register(StringBuilder.class, 28);
        kryo.register(Collections.EMPTY_LIST.getClass(), 29);
        kryo.register(Collections.EMPTY_MAP.getClass(), 30);
        kryo.register(Collections.EMPTY_SET.getClass(), 31);
        kryo.register(Collections.singleton(null).getClass(), 32);
        kryo.register(Collections.singletonList(null).getClass(), 33);
        kryo.register(Collections.singletonMap(null, null).getClass(), 34);
        kryo.register(TreeSet.class, 35);
        kryo.register(Collection.class, 36);
        kryo.register(TreeMap.class, 37);
        kryo.register(Map.class, 38);
        kryo.register(TimeZone.class, 39);
        kryo.register(Calendar.class, 40);
        kryo.register(Locale.class, 41);
        kryo.register(Charset.class, 42);
        kryo.register(URL.class, 43);
        kryo.register(List.of().getClass(), 44);
        kryo.register(PriorityQueue.class, 45);
        kryo.register(BitSet.class, 46);

        for (Class<?> clazz : registrations) {
            kryo.register(clazz, atomicInteger.getAndIncrement());
        }

        return kryo;
    }

    public void setRegistrationRequired(boolean registrationRequired) {
        this.registrationRequired = registrationRequired;
    }

    public abstract void returnKryo(Kryo kryo);

    public abstract Kryo getKryo();

}
