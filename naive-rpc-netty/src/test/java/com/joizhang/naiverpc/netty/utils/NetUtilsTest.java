package com.joizhang.naiverpc.netty.utils;

import org.junit.Test;

import java.net.InetSocketAddress;

import static org.junit.Assert.*;

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