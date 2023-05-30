package com.joizhang.naiverpc.remoting.transport;

import com.joizhang.naiverpc.remoting.server.RequestHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.joizhang.naiverpc.spi.ServiceSupportConstant.REQUEST_HANDLER_SERVICE_SUPPORT;

/**
 * 请求处理器的注册类
 */
@Slf4j
public class RequestHandlerRegistry {

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
            log.debug("Load request handler, type: {}, class: {}.",
                    requestHandler.type(), requestHandler.getClass().getCanonicalName());
        }
    }

    public RequestHandler get(byte type) {
        return handlerMap.get(type);
    }

}
