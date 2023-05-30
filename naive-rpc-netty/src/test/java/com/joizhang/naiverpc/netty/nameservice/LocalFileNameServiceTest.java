package com.joizhang.naiverpc.netty.nameservice;

import com.joizhang.naiverpc.netty.serialize.SerializeSupport;
import com.joizhang.naiverpc.netty.serialize.metadata.MetadataSerializer;
import com.joizhang.naiverpc.serialize.Serializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import static com.joizhang.naiverpc.spi.ServiceSupportConstant.SERIALIZER_SERVICE_SUPPORT;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LocalFileNameServiceTest {

    private static final Serializer metadataSerializer =
            SERIALIZER_SERVICE_SUPPORT.getService(MetadataSerializer.class.getCanonicalName());

    static RandomAccessFile randomAccessFile;

    @BeforeAll
    public static void beforeClass() throws Exception {
        randomAccessFile = new RandomAccessFile("test.data", "rw");
    }

    @Test
    public void testWriteObject() throws IOException {
        try (FileChannel fileChannel = randomAccessFile.getChannel()) {
            Metadata metadata = new Metadata();
            ArrayList<InetSocketAddress> addresses = new ArrayList<>();
            addresses.add(new InetSocketAddress("localhost", 9999));
            metadata.put(this.getClass().getCanonicalName(), addresses);
            byte[] bytes = SerializeSupport.serialize(metadataSerializer, metadata);
            assertNotNull(bytes);

            fileChannel.truncate(bytes.length);
            fileChannel.position(0L);
            fileChannel.write(ByteBuffer.wrap(bytes));
            fileChannel.force(true);
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

}