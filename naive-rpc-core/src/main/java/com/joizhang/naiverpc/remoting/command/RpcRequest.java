package com.joizhang.naiverpc.remoting.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RpcRequest {

    private final String interfaceName;

    private final String methodName;

    private final Object[] args;

    private final Class<?>[] argsTypes;

}
