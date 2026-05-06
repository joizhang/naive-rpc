package com.joizhang.naiverpc.netty.serialize.java;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.joizhang.naiverpc.netty.serialize.SerializeSupport;
import com.joizhang.naiverpc.netty.serialize.User;
import com.joizhang.naiverpc.serialize.Serializer;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class JavaSerializerTest {

    @Test
    public void testJavaSerializer() throws IOException, ClassNotFoundException {
        Serializer serializer = new JavaSerializer();

        int num = 1024;
        byte[] intBytes = SerializeSupport.serialize(serializer, num);
        assertNotNull(intBytes);
        int num1 = SerializeSupport.deserialize(serializer, intBytes, int.class);
        assertEquals(num, num1);

        User user = new User("Tom", 10);
        byte[] userBytes = SerializeSupport.serialize(serializer, user);
        assertNotNull(userBytes);
        User user1 = SerializeSupport.deserialize(serializer, userBytes, user.getClass());
        assertEquals(user, user1);
    }
}
