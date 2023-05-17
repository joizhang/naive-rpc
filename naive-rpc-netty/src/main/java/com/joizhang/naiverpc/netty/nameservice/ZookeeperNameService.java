package com.joizhang.naiverpc.netty.nameservice;

import com.joizhang.naiverpc.loadbalance.LoadBalance;
import com.joizhang.naiverpc.netty.loadbalance.RoundRobinLoadBalance;
import com.joizhang.naiverpc.nameservice.NameService;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.joizhang.naiverpc.spi.ServiceSupportConstant.LOAD_BALANCE_SERVICE_SUPPORT;

@Slf4j
public class ZookeeperNameService implements NameService {

    public static final LoadBalance LOAD_BALANCE = LOAD_BALANCE_SERVICE_SUPPORT.getService(RoundRobinLoadBalance.class.getCanonicalName());

    private static final Collection<String> SCHEMES = Collections.singleton("zookeeper");

    public static final String ROOT_PATH = "/naive";

    private static final int BASE_SLEEP_TIME = 1000;

    private static final int MAX_RETRIES = 3;

    public static final int ZK_CONNECTION_TIMEOUT = 10;

    private static CuratorFramework zkClient;

    @Override
    public Collection<String> supportedSchemes() {
        return SCHEMES;
    }

    @Override
    public void connect(URI nameServiceUri) {
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
            log.error(e.getMessage());
        }
    }

    @Override
    public void registerService(InetSocketAddress socketAddress, String serviceName) throws Exception {
        String servicePath = ROOT_PATH + '/' + serviceName + socketAddress.toString();
        Stat stat = zkClient.checkExists().forPath(servicePath);
        if (Objects.isNull(stat)) {
            zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(servicePath);
        }
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) throws Exception {
        String servicePath = ROOT_PATH + '/' + serviceName;
        List<String> serviceUrlList = zkClient.getChildren().forPath(servicePath);
        String targetServiceUrl = LOAD_BALANCE.select(serviceUrlList);
        String[] socketAddressArray = targetServiceUrl.split(":");
        String host = socketAddressArray[0];
        int port = Integer.parseInt(socketAddressArray[1]);
        return new InetSocketAddress(host, port);
    }

    @Override
    public void displayMetaData() {

    }

    @Override
    public void close() {
        zkClient.close();
    }
}
