package com.joizhang.naiverpc.remoting;

import java.io.Closeable;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public interface TransportClient extends Closeable {

    Transport createTransport(SocketAddress address, long connectionTimeout)
            throws InterruptedException, TimeoutException;

    Transport createTransport(SocketAddress address, long connectionTimeout, TimeUnit timeUnit)
            throws InterruptedException, TimeoutException;

    @Override
    void close();

}