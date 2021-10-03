package com.joizhang.naiverpc.netty.nameservice;

import com.joizhang.naiverpc.nameservice.NameService;
import com.joizhang.naiverpc.netty.serialize.MetadataSerializer;
import com.joizhang.naiverpc.netty.serialize.SerializeSupport;
import com.joizhang.naiverpc.serialize.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.joizhang.naiverpc.spi.ServiceSupportConstant.SERIALIZER_SERVICE_SUPPORT;

@Slf4j
public class LocalFileNameService implements NameService {

    private static final Collection<String> SCHEMES = Collections.singleton("file");

    private static final Serializer METADATA_SERIALIZER = SERIALIZER_SERVICE_SUPPORT.getService(MetadataSerializer.class.getCanonicalName());

    private File file;

    private static Metadata metadata = null;

    @Override
    public Collection<String> supportedSchemes() {
        return SCHEMES;
    }

    @Override
    public void connect(URI nameServiceUri) {
        if (SCHEMES.contains(nameServiceUri.getScheme())) {
            file = new File(nameServiceUri);
        } else {
            throw new RuntimeException("Unsupported scheme!");
        }
    }

    @Override
    public void registerService(InetSocketAddress socketAddress, String serviceName)
            throws IOException, ClassNotFoundException {
        log.info("Register service: {}, uri: {}.", serviceName, socketAddress);
        byte[] bytes;
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw");
             FileChannel fileChannel = raf.getChannel()) {
            FileLock lock = fileChannel.lock();
            try {
                long fileLength = raf.length();
                if (fileLength > Integer.MAX_VALUE) {
                    throw new IOException("The local file exceeds the maximum limit.");
                }
                if (fileLength > 0) {
                    bytes = new byte[(int) fileLength];
                    ByteBuffer buffer = ByteBuffer.wrap(bytes);
                    while (buffer.hasRemaining()) {
                        fileChannel.read(buffer);
                    }
                    metadata = SerializeSupport.deserialize(METADATA_SERIALIZER, bytes, Metadata.class);
                } else {
                    metadata = new Metadata();
                }

                List<InetSocketAddress> socketAddresses = metadata.computeIfAbsent(serviceName, k -> new ArrayList<>());
                if (!socketAddresses.contains(socketAddress)) {
                    socketAddresses.add(socketAddress);
                }

                bytes = SerializeSupport.serialize(METADATA_SERIALIZER, metadata);
                fileChannel.truncate(bytes.length);
                fileChannel.position(0L);
                fileChannel.write(ByteBuffer.wrap(bytes));
                fileChannel.force(true);
            } finally {
                lock.release();
            }
        }
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) throws IOException, ClassNotFoundException {
        Metadata metadata1;
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw");
             FileChannel fileChannel = raf.getChannel()) {
            FileLock lock = fileChannel.lock();
            try {
                byte[] bytes = new byte[(int) raf.length()];
                ByteBuffer buffer = ByteBuffer.wrap(bytes);
                while (buffer.hasRemaining()) {
                    fileChannel.read(buffer);
                }
                if (bytes.length > 0) {
                    metadata1 = SerializeSupport.deserialize(METADATA_SERIALIZER, bytes, Metadata.class);
                } else {
                    metadata1 = new Metadata();
                }
            } finally {
                lock.release();
            }
        }

        List<InetSocketAddress> socketAddresses = metadata1.get(serviceName);
        if (null == socketAddresses || socketAddresses.isEmpty()) {
            return null;
        } else {
            return socketAddresses.get(ThreadLocalRandom.current().nextInt(socketAddresses.size()));
        }
    }

    @Override
    public void displayMetaData() {
        log.info(file.getPath());
        log.info(metadata.toString());
    }

    @Override
    public void close() {
    }
}
