package com.joizhang.naiverpc.proxy;

import com.joizhang.naiverpc.remoting.Transport;

public interface StubFactory {

    <T> T createStub(Transport transport, Class<T> serviceClass);

}
