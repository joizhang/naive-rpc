package com.joizhang.naiverpc.netty.nameservice;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.List;

public class Metadata extends HashMap<String, List<InetSocketAddress>> {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Metadata:").append("\n\n");
        for (Entry<String, List<InetSocketAddress>> entry : entrySet()) {
            sb.append("\t").append("Classname: ").append(entry.getKey()).append("\n");
            sb.append("\t").append("Providers: ").append("\t");
            for (InetSocketAddress socketAddress : entry.getValue()) {
                sb.append("\t").append(socketAddress);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
