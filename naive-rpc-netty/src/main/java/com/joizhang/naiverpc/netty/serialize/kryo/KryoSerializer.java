package com.joizhang.naiverpc.netty.serialize.kryo;

import com.joizhang.naiverpc.serialize.ObjectInput;
import com.joizhang.naiverpc.serialize.ObjectOutput;
import com.joizhang.naiverpc.serialize.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.joizhang.naiverpc.netty.serialize.SerializerType.KRYO_SERIALIZATION_ID;

public class KryoSerializer implements Serializer {

    @Override
    public byte getContentTypeId() {
        return KRYO_SERIALIZATION_ID;
    }

    @Override
    public String getContentType() {
        return "x-application/kryo";
    }

    @Override
    public ObjectOutput serialize(OutputStream outputStream) throws IOException {
        return new KryoObjectOutput(outputStream);
    }

    @Override
    public ObjectInput deserialize(InputStream inputStream) throws IOException {
        return new KryoObjectInput(inputStream);
    }
}
