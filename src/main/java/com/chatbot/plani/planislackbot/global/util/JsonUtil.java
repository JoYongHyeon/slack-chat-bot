package com.chatbot.plani.planislackbot.global.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class JsonUtil {
    private JsonUtil() {}

    /**
     * JSON → Map<String, String> 변환(자주 쓰는 케이스용)
     */
    public static Map<String, String> toMap(String json, ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            return Map.of();
        }
    }

    /**
     * JSON → 특정 클래스 객체로 변환
     */
    public static <T> T toObject(String json, Class<T> clazz, ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * JSON → 복잡한 타입(예: Map, List, 제네릭)으로 변환
     */
    public static<T> T toObject(String json, TypeReference<T> typeReference, ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (Exception e) {
            return null;
        }
    }
}
