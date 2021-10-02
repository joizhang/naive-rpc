package com.joizhang.naiverpc.netty.proxy;

import com.joizhang.naiverpc.netty.utils.AtomicPositiveInteger;

public class RequestIdSupport {

    private final static AtomicPositiveInteger NEXT_REQUEST_ID = new AtomicPositiveInteger(0);

    public static int next() {
        return NEXT_REQUEST_ID.getAndIncrement();
    }

}
