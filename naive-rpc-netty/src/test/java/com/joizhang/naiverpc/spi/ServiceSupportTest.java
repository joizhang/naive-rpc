package com.joizhang.naiverpc.spi;

import com.joizhang.naiverpc.netty.serialize.JavaSerializer;
import com.joizhang.naiverpc.netty.serialize.NativeJavaSerializer;
import com.joizhang.naiverpc.serialize.Serializer;
import org.junit.Test;

import java.util.Collection;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

public class ServiceSupportTest {

    @Test
    public void load() {
        ServiceSupport<Serializer> serviceSupport = ServiceSupport.getServiceSupport(Serializer.class);
        Serializer serializer = serviceSupport.getService(JavaSerializer.class.getCanonicalName());
        assertEquals(serializer.getClass(), JavaSerializer.class);
        serializer = serviceSupport.getService(NativeJavaSerializer.class.getCanonicalName());
        assertEquals(serializer.getClass(), NativeJavaSerializer.class);
    }

    @Test
    public void getAllService() {
        ServiceSupport<Serializer> serviceSupport = ServiceSupport.getServiceSupport(Serializer.class);
        Collection<Serializer> serializers = serviceSupport.getAllService();
        assertEquals(2, serializers.size());
    }

    @Test
    public void testServiceLoader() {
//        StreamSupport.stream(ServiceLoader.load(Serializer.class).spliterator(), false)
//                .map(ServiceSupport::singletonFilter)
//                .forEach(System.out::println);
    }

}
