package com.joizhang.naiverpc.netty.serialize;

import com.joizhang.naiverpc.serialize.ObjectInput;
import com.joizhang.naiverpc.serialize.ObjectOutput;
import com.joizhang.naiverpc.serialize.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
public class SerializeSupport {

    /**
     * 序列化
     *
     * @param serializer 序列化器
     * @param t          待序列化数据
     * @param <T>        type
     * @return 字节数组
     * @throws IOException
     */
    public static <T> byte[] serialize(Serializer serializer, T t) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            ObjectOutput objectOutput = serializer.serialize(byteArrayOutputStream);
            objectOutput.writeObject(t);
            objectOutput.flushBuffer();
            return byteArrayOutputStream.toByteArray();
        }
    }

    /**
     * 反序列化
     *
     * @param serializer 序列化器
     * @param bytes      待反序列化的字节数组
     * @param clazz      对象类型
     * @param <T>        type
     * @return 对象
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static <T> T deserialize(Serializer serializer, byte[] bytes, Class<T> clazz)
            throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
            ObjectInput objectInput = serializer.deserialize(inputStream);
            return objectInput.readObject(clazz);
        }
    }

}
