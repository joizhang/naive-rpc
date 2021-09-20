package com.joizhang.naiverpc.remoting.transport;

import com.joizhang.naiverpc.spi.ServiceSupport;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RequestHandlerRegistry {

    private static final ServiceSupport<RequestHandler> REQUEST_HANDLER_SERVICE_SUPPORT = ServiceSupport.getServiceSupport(RequestHandler.class);

    private final Map<Byte, RequestHandler> handlerMap = new HashMap<>();

    private static RequestHandlerRegistry instance = null;

    public static RequestHandlerRegistry getInstance() {
        if (null == instance) {
            instance = new RequestHandlerRegistry();
        }
        return instance;
    }

    private RequestHandlerRegistry() {
        Collection<RequestHandler> requestHandlers = REQUEST_HANDLER_SERVICE_SUPPORT.getAllService();
        for (RequestHandler requestHandler : requestHandlers) {
            handlerMap.put(requestHandler.type(), requestHandler);
            log.info("Load request handler, type: {}, class: {}.",
                    requestHandler.type(), requestHandler.getClass().getCanonicalName());
        }
    }

    public RequestHandler get(int type) {
        return handlerMap.get(type);
    }

}
