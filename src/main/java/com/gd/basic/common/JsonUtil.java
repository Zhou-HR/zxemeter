package com.gd.basic.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * @author ZhouHR
 */
@Log4j2
public final class JsonUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String EMPTY = "";

    public static String toJson(Object obj) {
        if (obj == null) {
            return EMPTY;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error(e);
            return EMPTY;
        }
    }

    public static Map<String, Object> fromJsonToMap(String jsonStr) {
        if (StringUtils.isEmpty(jsonStr)) {
            return Collections.EMPTY_MAP;
        }

        try {
            return OBJECT_MAPPER.readValue(jsonStr, Map.class);
        } catch (IOException e) {
            log.error("", e);
            return Collections.EMPTY_MAP;
        }
    }

    public static <T> List<T> fromJsonToList(String jsonStr, Class<T> beanClass) {
        if (StringUtils.isEmpty(jsonStr)) {
            return null;
        }

        try {
            return OBJECT_MAPPER.readValue(jsonStr, OBJECT_MAPPER.getTypeFactory().constructCollectionType(ArrayList.class, beanClass));
        } catch (IOException e) {
            log.error("", e);
            return null;
        }
    }
}
