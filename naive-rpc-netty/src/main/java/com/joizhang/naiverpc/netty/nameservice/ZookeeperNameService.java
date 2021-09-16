package com.joizhang.naiverpc.netty.nameservice;

import com.joizhang.naiverpc.nameservice.NameService;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;

public class ZookeeperNameService implements NameService {

    @Override
    public Collection<String> supportedSchemes() {
        return null;
    }

    @Override
    public void connect(URI nameServiceUri) {

    }

    @Override
    public void registerService(String serviceName, URI uri) throws IOException {

    }

    @Override
    public URI lookupService(String serviceName) throws IOException {
        return null;
    }

    @Override
    public void displayMetaData() {

    }

}
