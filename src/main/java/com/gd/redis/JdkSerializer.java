package com.gd.redis;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author tanshuai
 */
@Slf4j
public class JdkSerializer implements Serializer {

    @Override
    public byte[] serialize(Object o) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(128);
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteStream);
            objectOutputStream.writeObject(o);
            objectOutputStream.flush();
            return byteStream.toByteArray();
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialize(byte[] bytes) {
        try {
            ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteStream);
            return (T) objectInputStream.readObject();
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }
}
