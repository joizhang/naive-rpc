package com.joizhang.naiverpc.client;

import com.joizhang.naiverpc.transport.Transport;

public interface StubFactory {

    <T> T createStub(Transport transport, Class<T> serviceClass);

}
