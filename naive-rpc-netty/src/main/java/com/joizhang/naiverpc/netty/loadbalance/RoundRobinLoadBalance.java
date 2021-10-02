package com.joizhang.naiverpc.netty.loadbalance;

import com.joizhang.naiverpc.loadbalance.AbstractLoadBalance;
import com.joizhang.naiverpc.netty.utils.AtomicPositiveInteger;

import java.util.List;

public class RoundRobinLoadBalance extends AbstractLoadBalance {

    private static final AtomicPositiveInteger ROUND = new AtomicPositiveInteger();

    @Override
    protected String doSelect(List<String> serviceAddresses) {
        int length = serviceAddresses.size();
        int round = ROUND.getAndIncrement();
        return serviceAddresses.get(round % length);
    }

}
