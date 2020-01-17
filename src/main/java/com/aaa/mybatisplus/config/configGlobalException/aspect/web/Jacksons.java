package com.aaa.mybatisplus.config.configGlobalException.aspect.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

/**
 * @author soul
 */
public final class Jacksons {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static{
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    private Jacksons() {
    }

    public static <T> String parse(T obj) {
        try {
            if (obj == null) {
                return null;
            }
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static <T> String parseInPrettyMode(T obj) {
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }
    public static <T> T toBean(String json,Class<T> valueType){
        if (StringUtils.isEmpty(json) || null==valueType){
            return null;
        }
        try {
            return MAPPER.readValue(json,valueType);
        }catch (Exception e){
            throw new IllegalArgumentException(e);
        }
    }
    public static ObjectMapper getMapper() {
        return MAPPER;
    }
}
