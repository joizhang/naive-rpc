package com.joizhang.naiverpc.netty.serialize;

public interface SerializerType {

    byte META_DATA_SERIALIZATION_ID = 1;
    byte NATIVE_JAVA_SERIALIZATION_ID = 2;
    byte JAVA_SERIALIZATION_ID = 3;
    byte HESSIAN2_SERIALIZATION_ID = 4;
    byte JSON_SERIALIZATION_ID = 5;

}
