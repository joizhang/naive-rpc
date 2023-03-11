package com.joizhang.naiverpc.netty.loadbalance;

import com.joizhang.naiverpc.loadbalance.AbstractLoadBalance;
import com.joizhang.naiverpc.utils.AtomicPositiveInteger;

import java.util.List;

/**
 * Round Robin load balance.
 */
public class RoundRobinLoadBalance extends AbstractLoadBalance {

    private static final AtomicPositiveInteger ROUND = new AtomicPositiveInteger();

    @Override
    protected String doSelect(List<String> remoteServiceAddresses) {
        int length = remoteServiceAddresses.size();
        int round = ROUND.getAndIncrement();
        return remoteServiceAddresses.get(round % length);
    }

}
