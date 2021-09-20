package com.joizhang.naiverpc.netty.nameservice;

import com.joizhang.naiverpc.netty.serialize.MetadataSerializer;
import com.joizhang.naiverpc.netty.serialize.SerializeSupport;
import com.joizhang.naiverpc.serialize.Serializer;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;

import static com.joizhang.naiverpc.spi.ServiceSupportConstant.SERIALIZER_SERVICE_SUPPORT;

public class LocalFileNameServiceTest {

    private static final Serializer metadataSerializer = SERIALIZER_SERVICE_SUPPORT.getService(MetadataSerializer.class.getCanonicalName());

    RandomAccessFile randomAccessFile;

    @Before
    public void beforeClass() throws Exception {
        randomAccessFile = new RandomAccessFile("E:\\test.data", "rw");
    }

    @Test
    public void testWriteObject() throws IOException {
        try (FileChannel fileChannel = randomAccessFile.getChannel()) {
            Metadata metadata = new Metadata();
            metadata.put(this.getClass().getCanonicalName(),
                    new ArrayList<>(Collections.singletonList(new URI("rpc://localhost:9000"))));
            byte[] bytes = SerializeSupport.serialize(metadataSerializer, metadata);
            Assert.assertNotNull(bytes);

            fileChannel.truncate(bytes.length);
            fileChannel.position(0L);
            fileChannel.write(ByteBuffer.wrap(bytes));
            fileChannel.force(true);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadObject() throws IOException {
        try (FileChannel fileChannel = randomAccessFile.getChannel()) {
            long length = randomAccessFile.length();
            if (length > 0) {
                byte[] bytes = new byte[(int) length];
                ByteBuffer buffer = ByteBuffer.wrap(bytes);
                while (buffer.hasRemaining()) {
                    fileChannel.read(buffer);
                }
                Metadata metadata = SerializeSupport.deserialize(metadataSerializer, bytes, Metadata.class);
                System.out.println(metadata);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @ToString
    @AllArgsConstructor
    static class User implements Serializable {
        private String username;
        private int age;
    }

}