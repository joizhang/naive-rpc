package com.joizhang.naiverpc.loadbalance;

import com.joizhang.naiverpc.spi.SPI;

import java.util.List;

/**
 * 负载均衡
 */
@SPI
public interface LoadBalance {

    /**
     * select one remote address in list.
     *
     * @param remoteServiceAddresses addresses
     * @return selected address
     */
    String select(List<String> remoteServiceAddresses);

}
