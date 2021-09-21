package com.joizhang.naiverpc.netty.remoting.command;

public interface MessageType {

    byte TYPE_RPC = 0x10;

    byte REQUEST_TYPE = 0x11;

    byte RESPONSE_TYPE = 0x12;

    /**
     * ping
     */
    byte HEARTBEAT_REQUEST_TYPE = 0x13;

    /**
     * pong
     */
    byte HEARTBEAT_RESPONSE_TYPE = 0x14;

}
