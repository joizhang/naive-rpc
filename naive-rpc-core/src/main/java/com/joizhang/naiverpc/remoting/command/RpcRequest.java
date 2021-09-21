package com.joizhang.naiverpc.remoting.command;

import lombok.Builder;
import lombok.Getter;

import java.io.Externalizable;
import java.io.Serializable;

@Getter
@Builder
public class RpcRequest implements Serializable {

    private final String interfaceName;

    private final String methodName;

    private final Object[] args;

    private final Class<?>[] argsTypes;

}
