package com.joizhang.naiverpc.netty.serialize.gson;

import com.google.gson.Gson;
import com.joizhang.naiverpc.serialize.ObjectOutput;

import java.io.*;

public class GsonObjectOutput implements ObjectOutput {

    private final PrintWriter writer;

    private final Gson gson;

    public GsonObjectOutput(OutputStream outputStream) {
        this(new OutputStreamWriter(outputStream));
    }

    public GsonObjectOutput(Writer writer) {
        this.writer = new PrintWriter(writer);
        this.gson = new Gson();
    }

    @Override
    public void writeBool(boolean v) throws IOException {
        writeObject(v);
    }

    @Override
    public void writeByte(byte v) throws IOException {
        writeObject(v);
    }

    @Override
    public void writeShort(short v) throws IOException {
        writeObject(v);
    }

    @Override
    public void writeInt(int v) throws IOException {
        writeObject(v);
    }

    @Override
    public void writeLong(long v) throws IOException {
        writeObject(v);
    }

    @Override
    public void writeFloat(float v) throws IOException {
        writeObject(v);
    }

    @Override
    public void writeDouble(double v) throws IOException {
        writeObject(v);
    }

    @Override
    public void writeUTF(String v) throws IOException {
        writeObject(v);
    }

    @Override
    public void writeBytes(byte[] v) throws IOException {
        writer.println(new String(v));
    }

    @Override
    public void writeBytes(byte[] v, int off, int len) throws IOException {
        writer.println(new String(v, off, len));
    }

    @Override
    public void flushBuffer() throws IOException {
        writer.flush();
    }

    @Override
    public void writeObject(Object obj) throws IOException {
        char[] json = gson.toJson(obj).toCharArray();
        writer.write(json, 0, json.length);
        writer.println();
        writer.flush();
    }

}
