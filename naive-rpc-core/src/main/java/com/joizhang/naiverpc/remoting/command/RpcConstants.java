package com.joizhang.naiverpc.remoting.command;

public interface RpcConstants {

    byte[] MAGIC_NUMBER = {(byte) 'n', (byte) 'a', (byte) 'i', (byte) 'v'};

    byte PADDING = 0x00;

    int MAX_FRAME_LENGTH = 8 * 1024 * 1024;

}
