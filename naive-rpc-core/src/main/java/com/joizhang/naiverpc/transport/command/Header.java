package com.joizhang.naiverpc.transport.command;

public class Header {

    private int type;
    private int version;
    private int requestId;

    public Header() {
    }

    public Header(int type, int version, int requestId) {
        this.type = type;
        this.version = version;
        this.requestId = requestId;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getType() {
        return type;
    }

    public int length() {
        return Integer.BYTES + Integer.BYTES + Integer.BYTES;
    }

    public void setType(int type) {
        this.type = type;
    }

}
