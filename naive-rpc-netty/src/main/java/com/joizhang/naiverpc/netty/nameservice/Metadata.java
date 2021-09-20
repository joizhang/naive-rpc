package com.joizhang.naiverpc.netty.nameservice;

import java.net.URI;
import java.util.HashMap;
import java.util.List;

public class Metadata extends HashMap<String, List<URI>> {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Metadata:").append("\n\n");
        for (Entry<String, List<URI>> entry : entrySet()) {
            sb.append("\t").append("Classname: ").append(entry.getKey()).append("\n");
            sb.append("\t").append("URIs: ").append("\t");
            for (URI uri : entry.getValue()) {
                sb.append("\t").append(uri);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
