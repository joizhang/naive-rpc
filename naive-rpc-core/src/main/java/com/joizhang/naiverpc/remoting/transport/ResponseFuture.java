package com.joizhang.naiverpc.remoting.transport;

import com.joizhang.naiverpc.remoting.command.Command;
import lombok.Getter;

import java.util.concurrent.CompletableFuture;

@Getter
public class ResponseFuture {

    private final int requestId;

    private final CompletableFuture<Command> future;

    private final long timestamp;

    public ResponseFuture(int requestId, CompletableFuture<Command> future) {
        this.requestId = requestId;
        this.future = future;
        this.timestamp = System.nanoTime();
    }

}
