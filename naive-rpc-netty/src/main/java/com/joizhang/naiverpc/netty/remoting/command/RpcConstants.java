package com.joizhang.naiverpc.netty.remoting.command;

public interface RpcConstants {

    byte RPC_VERSION = 0x01;

    byte[] MAGIC_NUMBER = {(byte) 'n', (byte) 'a', (byte) 'i', (byte) 'v'};

    byte PADDING = 0x00;

    int MAX_FRAME_LENGTH = 8 * 1024 * 1024;

    byte TOTAL_LENGTH = 16;

    int HEAD_LENGTH = 16;

}
