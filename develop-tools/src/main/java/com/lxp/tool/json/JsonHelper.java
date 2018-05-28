package com.lxp.tool.json;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelper {
    private static final Logger LOG = LoggerFactory.getLogger(JsonHelper.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.disable(FAIL_ON_UNKNOWN_PROPERTIES);
        OBJECT_MAPPER.setSerializationInclusion(NON_NULL);
    }

    public static String toString(Object obj) {
        String rtn = null;
        try {
            rtn = OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LOG.error(e.getMessage(), e);
        }
        return rtn;
    }

    public static <T> T toObject(Class<T> clazz, String content) {
        T rtn = null;
        try {
            rtn = OBJECT_MAPPER.readValue(content, clazz);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return rtn;
    }

    public static <T> T toObject(Class<T> clazz, InputStream inputStream) {
        T rtn = null;
        try {
            rtn = OBJECT_MAPPER.readValue(inputStream, clazz);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return rtn;
    }
}
