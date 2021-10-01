package com.joizhang.naiverpc.netty.nameservice;

import com.joizhang.naiverpc.loadbalance.LoadBalance;
import com.joizhang.naiverpc.loadbalance.RoundRobinLoadBalance;
import com.joizhang.naiverpc.nameservice.NameService;
import com.joizhang.naiverpc.netty.utils.CuratorUtils;
import com.joizhang.naiverpc.utils.NaiveRpcPropertiesSingleton;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.zookeeper.CreateMode;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static com.joizhang.naiverpc.spi.ServiceSupportConstant.LOAD_BALANCE_SERVICE_SUPPORT;

public class ZookeeperNameService implements NameService {


    public static final NaiveRpcPropertiesSingleton PROPERTIES_SINGLETON = NaiveRpcPropertiesSingleton.getInstance();

    public static final LoadBalance LOAD_BALANCE = LOAD_BALANCE_SERVICE_SUPPORT.getService(RoundRobinLoadBalance.class.getCanonicalName());

    private static final Collection<String> SCHEMES = Collections.singleton("zookeeper");

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
    public void registerService(String serviceName, URI uri) throws Exception {
        String zookeeperAddress = PROPERTIES_SINGLETON.getStringValueOrDefault(ZK_ADDRESS, DEFAULT_ZK_ADDRESS);
        CuratorFramework zkClient = CuratorUtils.getZkClient(zookeeperAddress);
        String servicePath = ROOT_PATH + '/' + serviceName + '/' + uri.toString();
        zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(servicePath);
    }

    @Override
    public URI lookupService(String serviceName) throws Exception {
        String zookeeperAddress = PROPERTIES_SINGLETON.getStringValueOrDefault(ZK_ADDRESS, DEFAULT_ZK_ADDRESS);
        CuratorFramework zkClient = CuratorUtils.getZkClient(zookeeperAddress);
        String servicePath = ROOT_PATH + '/' + serviceName;
        List<String> serviceUrlList = zkClient.getChildren().forPath(servicePath);
        String targetServiceUrl = LOAD_BALANCE.select(serviceUrlList);
        return new URI(targetServiceUrl);
    }

    @Override
    public void displayMetaData() {

    }

}
