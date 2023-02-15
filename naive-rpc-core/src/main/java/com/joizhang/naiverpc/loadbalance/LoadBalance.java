package com.joizhang.naiverpc.loadbalance;

import com.joizhang.naiverpc.spi.SPI;

import java.util.List;

/**
 * 负载均衡
 */
@SPI
public interface LoadBalance {

    /**
     * select one address in list.
     *
     * @param serviceAddresses addresses
     * @return selected address
     */
    String select(List<String> serviceAddresses);

}
