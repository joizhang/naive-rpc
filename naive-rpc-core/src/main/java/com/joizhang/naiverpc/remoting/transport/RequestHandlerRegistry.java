package com.joizhang.naiverpc.remoting.transport;

import com.joizhang.naiverpc.remoting.server.RequestHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static com.joizhang.naiverpc.spi.ServiceSupportConstant.REQUEST_HANDLER_SERVICE_SUPPORT;

/**
 * 请求处理器的注册类
 */
@Slf4j
public class RequestHandlerRegistry {

    private final Map<Byte, RequestHandler> handlerMap;

    private RequestHandlerRegistry() {
        Collection<RequestHandler> requestHandlers = REQUEST_HANDLER_SERVICE_SUPPORT.getAllService();
        Map<Byte, RequestHandler> map = new java.util.HashMap<>();
        for (RequestHandler requestHandler : requestHandlers) {
            map.put(requestHandler.type(), requestHandler);
            log.debug("Load request handler, type: {}, class: {}.",
                    requestHandler.type(), requestHandler.getClass().getCanonicalName());
        }
        this.handlerMap = Collections.unmodifiableMap(map);
    }

    private static class Holder {
        private static final RequestHandlerRegistry INSTANCE = new RequestHandlerRegistry();
    }

    public static RequestHandlerRegistry getInstance() {
        return Holder.INSTANCE;
    }

    public RequestHandler get(byte type) {
        return handlerMap.get(type);
    }

}
