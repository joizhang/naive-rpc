package com.joizhang.naiverpc.serialize;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public interface ObjectInput extends DataInput {

    /**
     * Consider use {@link #readObject(Class)} or {@link #readObject(Class, Type)} where possible
     *
     * @return object
     * @throws IOException            if an I/O error occurs
     * @throws ClassNotFoundException if an ClassNotFoundException occurs
     */
    @Deprecated
    Object readObject() throws IOException, ClassNotFoundException;

    /**
     * read object
     *
     * @param cls object class
     * @return object
     * @throws IOException            if an I/O error occurs
     * @throws ClassNotFoundException if an ClassNotFoundException occurs
     */
    <T> T readObject(Class<T> cls) throws IOException, ClassNotFoundException;

    /**
     * read object
     *
     * @param cls  object class
     * @param type object type
     * @return object
     * @throws IOException            if an I/O error occurs
     * @throws ClassNotFoundException if an ClassNotFoundException occurs
     */
    <T> T readObject(Class<T> cls, Type type) throws IOException, ClassNotFoundException;

}
