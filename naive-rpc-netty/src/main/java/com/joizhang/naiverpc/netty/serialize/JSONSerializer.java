package com.joizhang.naiverpc.netty.serialize;

import com.joizhang.naiverpc.serialize.ObjectInput;
import com.joizhang.naiverpc.serialize.ObjectOutput;
import com.joizhang.naiverpc.serialize.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class JSONSerializer implements Serializer {
    @Override
    public byte getContentTypeId() {
        return 0;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ObjectOutput serialize(OutputStream outputStream) throws IOException {
        return null;
    }

    @Override
    public ObjectInput deserialize(InputStream inputStream) throws IOException {
        return null;
    }
}
