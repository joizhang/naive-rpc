package com.joizhang.naiverpc.loadbalance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinLoadBalance extends AbstractLoadBalance {

    private static final AtomicInteger ROUND = new AtomicInteger();

    @Override
    protected String doSelect(List<String> serviceAddresses) {
        int length = serviceAddresses.size();
        int round = Math.abs(ROUND.getAndIncrement());
        return serviceAddresses.get(round % length);
    }

}
