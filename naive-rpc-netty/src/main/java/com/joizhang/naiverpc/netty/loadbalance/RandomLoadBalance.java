package com.joizhang.naiverpc.netty.loadbalance;

import com.joizhang.naiverpc.loadbalance.AbstractLoadBalance;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomLoadBalance extends AbstractLoadBalance {

    @Override
    protected String doSelect(List<String> serviceAddresses) {
        int length = serviceAddresses.size();
        return serviceAddresses.get(ThreadLocalRandom.current().nextInt(length));
    }

}
