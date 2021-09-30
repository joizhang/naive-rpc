package com.joizhang.naiverpc.netty.nameservice;

import com.joizhang.naiverpc.nameservice.NameService;
import com.joizhang.naiverpc.netty.utils.CuratorUtils;
import com.joizhang.naiverpc.utils.NaiveRpcPropertiesSingleton;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

public class ZookeeperNameService implements NameService {

    private static final Collection<String> SCHEMES = Collections.singleton("zookeeper");

    public static final NaiveRpcPropertiesSingleton PROPERTIES_SINGLETON = NaiveRpcPropertiesSingleton.getInstance();

    private static final String DEFAULT_ZK_ADDRESS = "127.0.0.1:2181";

    public static final String ZK_ADDRESS = "naive.register.address";

    public static final String ROOT_PATH = "/naive";


    @Override
    public Collection<String> supportedSchemes() {
        return SCHEMES;
    }

    @Override
    public void connect(URI nameServiceUri) {

    }

    @Override
    public void registerService(String serviceName, URI uri) throws IOException {
        String zookeeperAddress = PROPERTIES_SINGLETON.getStringValueOrDefault(ZK_ADDRESS, DEFAULT_ZK_ADDRESS);
        CuratorFramework zkClient = CuratorUtils.getZkClient(zookeeperAddress);
    }

    @Override
    public URI lookupService(String serviceName) throws IOException {
        return null;
    }

    @Override
    public void displayMetaData() {

    }

}
