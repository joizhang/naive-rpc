package com.joizhang.naiverpc.serialize;

import com.joizhang.naiverpc.spi.Singleton;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Singleton
public interface Serializer {

    /**
     * @return 用一个字节标识对象类型，每种类型的数据应该具有不同的类型值
     */
    byte getContentTypeId();

    /**
     * @return 序列化类型
     */
    String getContentType();

    /**
     * @return 序列化对象。将给定的对象序列化成字节数组
     */
    ObjectOutput serialize(OutputStream outputStream) throws IOException;

    /**
     * @return 反序列化对象
     */
    ObjectInput deserialize(InputStream inputStream) throws IOException;

}
