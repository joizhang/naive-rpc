package com.joizhang.naiverpc.netty.loadbalance;

import com.joizhang.naiverpc.loadbalance.AbstractLoadBalance;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Random load balance.
 */
public class RandomLoadBalance extends AbstractLoadBalance {

    @Override
    protected String doSelect(List<String> remoteServiceAddresses) {
        int length = remoteServiceAddresses.size();
        return remoteServiceAddresses.get(ThreadLocalRandom.current().nextInt(length));
    }

}
