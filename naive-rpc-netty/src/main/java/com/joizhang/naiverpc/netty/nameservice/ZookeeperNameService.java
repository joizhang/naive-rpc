package com.joizhang.naiverpc.netty.nameservice;

import com.joizhang.naiverpc.nameservice.NameService;
import org.apache.curator.framework.CuratorFramework;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;

public class ZookeeperNameService implements NameService {

    private static final Collection<String> SCHEMES = Collections.singleton("zookeeper");

    private static final String DEFAULT_ZK_ADDRESS = "127.0.0.1:2181";
    public static final String ZK_ADDRESS = "naive.register.address";

    private static CuratorFramework zkClient;

    @Override
    public Collection<String> supportedSchemes() {
        return SCHEMES;
    }

    @Override
    public void connect(URI nameServiceUri) {

    }

    @Override
    public void registerService(String serviceName, URI uri) throws IOException {

    }

    @Override
    public URI lookupService(String serviceName) throws IOException {
        return null;
    }

    @Override
    public void displayMetaData() {

    }

}
