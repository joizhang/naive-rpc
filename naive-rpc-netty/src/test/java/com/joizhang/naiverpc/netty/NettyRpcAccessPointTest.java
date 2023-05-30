package com.joizhang.naiverpc.netty;

import com.joizhang.naiverpc.nameservice.NameService;
import com.joizhang.naiverpc.netty.nameservice.LocalFileNameService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class NettyRpcAccessPointTest {

    @Test
    public void testGetNameService() {
        try (NettyRpcAccessPoint rpcAccessPoint = new NettyRpcAccessPoint()) {
            NameService nameService = rpcAccessPoint.getNameService(NettyRpcAccessPointTest.class);
            assertTrue(nameService instanceof LocalFileNameService);
        }
    }

}