package com.joizhang.naiverpc.netty.serialize.java;

import com.joizhang.naiverpc.netty.serialize.nativejava.NativeJavaObjectOutput;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class JavaObjectOutput extends NativeJavaObjectOutput {

    public JavaObjectOutput(OutputStream os) throws IOException {
        super(new ObjectOutputStream(os));
    }

    @Override
    public void writeUTF(String v) throws IOException {
        if (v == null) {
            getObjectOutputStream().writeInt(-1);
        } else {
            getObjectOutputStream().writeInt(v.length());
            getObjectOutputStream().writeUTF(v);
        }
    }

    @Override
    public void writeObject(Object obj) throws IOException {
        if (obj == null) {
            getObjectOutputStream().writeByte(0);
        } else {
            getObjectOutputStream().writeByte(1);
            getObjectOutputStream().writeObject(obj);
        }
    }

    @Override
    public void flushBuffer() throws IOException {
        getObjectOutputStream().flush();
    }

}
