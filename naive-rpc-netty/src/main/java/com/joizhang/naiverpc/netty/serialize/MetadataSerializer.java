package com.joizhang.naiverpc.netty.serialize;

import com.joizhang.naiverpc.serialize.ObjectInput;
import com.joizhang.naiverpc.serialize.ObjectOutput;
import com.joizhang.naiverpc.serialize.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.joizhang.naiverpc.serialize.Constants.META_DATA_SERIALIZATION_ID;

public class MetadataSerializer implements Serializer {

    private final Serializer serializer = new JavaSerializer();

    @Override
    public byte getContentTypeId() {
        return META_DATA_SERIALIZATION_ID;
    }

    @Override
    public String getContentType() {
        return "x-application/metadata";
    }

    @Override
    public ObjectOutput serialize(OutputStream outputStream) throws IOException {
        return serializer.serialize(outputStream);
    }

    @Override
    public ObjectInput deserialize(InputStream inputStream) throws IOException {
        return serializer.deserialize(inputStream);
    }

}
