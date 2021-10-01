package com.joizhang.naiverpc.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NaiveRpcPropertiesSingletonTest {

    @Test
    public void testPathGet() {
        NaiveRpcPropertiesSingleton instance = NaiveRpcPropertiesSingleton.getInstance();
        String applicationName = instance.getStringValue("naive.application.name");
        assertEquals("naive-rpc-netty", applicationName);
    }

}