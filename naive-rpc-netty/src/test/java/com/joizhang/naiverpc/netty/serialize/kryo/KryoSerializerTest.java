package com.joizhang.naiverpc.netty.serialize.kryo;

import com.joizhang.naiverpc.netty.serialize.SerializeSupport;
import com.joizhang.naiverpc.netty.serialize.User;
import com.joizhang.naiverpc.netty.serialize.kryo.utils.KryoUtils;
import com.joizhang.naiverpc.serialize.Serializer;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class KryoSerializerTest {

    @Test
    public void testKryoSerializer() throws IOException, ClassNotFoundException {
        Serializer serializer = new KryoSerializer();
        KryoUtils.register(User.class);

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