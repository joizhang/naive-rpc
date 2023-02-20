package com.joizhang.naiverpc.netty.serialize.gson;

import com.joizhang.naiverpc.serialize.ObjectInput;
import com.joizhang.naiverpc.serialize.ObjectOutput;
import com.joizhang.naiverpc.serialize.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.joizhang.naiverpc.netty.serialize.SerializerType.GSON_SERIALIZATION_ID;

public class GsonSerializer implements Serializer {

    @Override
    public byte getContentTypeId() {
        return GSON_SERIALIZATION_ID;
    }

    @Override
    public String getContentType() {
        return "text/json";
    }

    @Override
    public ObjectOutput serialize(OutputStream outputStream) throws IOException {
        return new GsonObjectOutput(outputStream);
    }

    @Override
    public ObjectInput deserialize(InputStream inputStream) throws IOException {
        return new GsonObjectInput(inputStream);
    }

}
