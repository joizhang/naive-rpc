package com.joizhang.naiverpc.netty.nameservice;

import com.joizhang.naiverpc.loadbalance.LoadBalance;
import com.joizhang.naiverpc.loadbalance.RoundRobinLoadBalance;
import com.joizhang.naiverpc.nameservice.NameService;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.joizhang.naiverpc.spi.ServiceSupportConstant.LOAD_BALANCE_SERVICE_SUPPORT;

public class ZookeeperNameService implements NameService {

    public static final LoadBalance LOAD_BALANCE = LOAD_BALANCE_SERVICE_SUPPORT.getService(RoundRobinLoadBalance.class.getCanonicalName());

    private static final Collection<String> SCHEMES = Collections.singleton("zookeeper");

    private static final String DEFAULT_ZK_ADDRESS = "127.0.0.1:2181";

    public static final String ROOT_PATH = "/naive";

    private static final int BASE_SLEEP_TIME = 1000;

    private static final int MAX_RETRIES = 3;

    public static final int ZK_CONNECTION_TIMEOUT = 10;

    private static CuratorFramework zkClient;

    private URI uri;

    @Override
    public Collection<String> supportedSchemes() {
        return SCHEMES;
    }

    @Override
    public void connect(URI nameServiceUri) {
        this.uri = nameServiceUri;
        if (!SCHEMES.contains(nameServiceUri.getScheme())) {
            throw new RuntimeException("Unsupported scheme!");
        }
        if (zkClient != null) {
            return;
        }
        String zookeeperAddress = nameServiceUri.getAuthority();
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRIES);
        zkClient = CuratorFrameworkFactory.builder()
                .connectString(zookeeperAddress)
                .retryPolicy(retryPolicy)
                .build();
        zkClient.start();
        try {
            // wait 30s until connect to the zookeeper
            if (!zkClient.blockUntilConnected(ZK_CONNECTION_TIMEOUT, TimeUnit.SECONDS)) {
                throw new RuntimeException("Time out waiting to connect to ZK!");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registerService(String serviceName) throws Exception {
        String servicePath = ROOT_PATH + '/' + serviceName + '/' + this.uri.getAuthority();
        Stat stat = zkClient.checkExists().forPath(servicePath);
        if (Objects.isNull(stat)) {
            zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(servicePath);
        }
    }

    @Override
    public URI lookupService(String serviceName) throws Exception {
        String servicePath = ROOT_PATH + '/' + serviceName;
        List<String> serviceUrlList = zkClient.getChildren().forPath(servicePath);
        String targetServiceUrl = LOAD_BALANCE.select(serviceUrlList);
        return new URI(targetServiceUrl);
    }

    @Override
    public void displayMetaData() {

    }

}
