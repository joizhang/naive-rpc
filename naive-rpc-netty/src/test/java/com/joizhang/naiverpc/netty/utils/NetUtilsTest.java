package com.joizhang.naiverpc.netty.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.joizhang.naiverpc.utils.NetUtils;
import java.net.InetSocketAddress;
import org.junit.jupiter.api.Test;

public class NetUtilsTest {

    @Test
    public void getRandomPort() {
        System.out.println(NetUtils.getRandomPort());
    }

    @Test
    public void getLocalSocketAddress() {
        InetSocketAddress socketAddress = NetUtils.getLocalSocketAddress("localhost", 8080);
        assertEquals("0.0.0.0", socketAddress.getHostString());
    }

    @Test
    public void getLocalHost() {
        String localHost = NetUtils.getLocalHost();
        System.out.println(localHost);
    }
}
