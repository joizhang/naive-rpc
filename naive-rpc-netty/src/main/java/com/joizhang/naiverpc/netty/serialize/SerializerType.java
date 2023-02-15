package com.joizhang.naiverpc.netty.serialize;

public interface SerializerType {

    byte NATIVE_JAVA_SERIALIZATION_ID = 1;

    byte JAVA_SERIALIZATION_ID = 2;

    byte META_DATA_SERIALIZATION_ID = 3;

    byte GSON_SERIALIZATION_ID = 4;

    byte HESSIAN2_SERIALIZATION_ID = 5;

    byte KRYO_SERIALIZATION_ID = 6;

}
