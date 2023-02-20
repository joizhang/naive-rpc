package com.joizhang.naiverpc.loadbalance;

import java.util.List;

public abstract class AbstractLoadBalance implements LoadBalance {

    @Override
    public String select(List<String> remoteServiceAddresses) {
        if (remoteServiceAddresses == null || remoteServiceAddresses.isEmpty()) {
            return null;
        }
        if (remoteServiceAddresses.size() == 1) {
            return remoteServiceAddresses.get(0);
        }
        return doSelect(remoteServiceAddresses);
    }

    protected abstract String doSelect(List<String> remoteServiceAddresses);

}
