package com.joizhang.naiverpc.serialize;

import java.io.IOException;
import java.util.Map;

public interface ObjectOutput extends DataOutput {

    /**
     * write object.
     *
     * @param obj object.
     */
    void writeObject(Object obj) throws IOException;

    /**
     * The following methods are customized for the requirement of Dubbo's RPC protocol implementation. Legacy protocol
     * implementation will try to write Map, Throwable and Null value directly to the stream, which does not meet the
     * restrictions of all serialization protocols.
     *
     * <p>
     * See how ProtobufSerialization, KryoSerialization implemented these methods for more details.
     * <p>
     * <p>
     * The binding of RPC protocol and biz serialization protocol is not a good practice. Encoding of RPC protocol
     * should be highly independent and portable, easy to cross platforms and languages, for example, like the http headers,
     * restricting the content of headers / attachments to Ascii strings and uses ISO_8859_1 to encode them.
     * https://tools.ietf.org/html/rfc7540#section-8.1.2
     */
    default void writeThrowable(Object obj) throws IOException {
        writeObject(obj);
    }

    default void writeEvent(Object data) throws IOException {
        writeObject(data);
    }

    default void writeAttachments(Map<String, Object> attachments) throws IOException {
        writeObject(attachments);
    }

}
