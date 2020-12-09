package com.gd.redis;

import java.nio.charset.Charset;

/**
 * @author tanshuai
 */
public class StringSerializer implements Serializer {

    private final Charset charset = Charset.forName("UTF8");

    public byte[] serialize(Object s) {
        return (s == null || !(s instanceof String) ? null : ((String) s).getBytes(charset));
    }

    @SuppressWarnings("unchecked")
	public String deserialize(byte[] bytes) {
        return (bytes == null ? null : new String(bytes, charset));
    }
}
