package com.joizhang.naiverpc.utils;

import com.joizhang.naiverpc.config.NaiveRpcPropertiesSingleton;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NaiveRpcPropertiesSingletonTest {

    @Test
    public void testPathGet() {
        NaiveRpcPropertiesSingleton instance = NaiveRpcPropertiesSingleton.getInstance();
        String applicationName = instance.getStringValue("naiverpc.application.name");
        assertEquals("naive-rpc-netty", applicationName);
    }

}