package com.joizhang.naiverpc.netty.nameservice;

import com.joizhang.naiverpc.nameservice.NameService;
import com.joizhang.naiverpc.netty.serialize.MetadataSerializer;
import com.joizhang.naiverpc.netty.serialize.SerializeSupport;
import com.joizhang.naiverpc.serialize.ObjectInput;
import com.joizhang.naiverpc.serialize.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
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

    private File file = null;

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
    public void registerService(String serviceName, URI uri) throws IOException, ClassNotFoundException {
        log.info("Register service: {}, uri: {}.", serviceName, uri);
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

                List<URI> uris = metadata.computeIfAbsent(serviceName, k -> new ArrayList<>());
                if (!uris.contains(uri)) {
                    uris.add(uri);
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
    public URI lookupService(String serviceName) throws IOException, ClassNotFoundException {
        Metadata metadata;
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
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
                    ObjectInput deserialize = METADATA_SERIALIZER.deserialize(inputStream);
                    metadata = deserialize.readObject(Metadata.class);
                } else {
                    metadata = new Metadata();
                }
            } finally {
                lock.release();
            }
        }

        List<URI> uris = metadata.get(serviceName);
        if (null == uris || uris.isEmpty()) {
            return null;
        } else {
            return uris.get(ThreadLocalRandom.current().nextInt(uris.size()));
        }
    }

    @Override
    public void displayMetaData() {
        log.info(file.getPath());
        log.info(metadata.toString());
    }
}
