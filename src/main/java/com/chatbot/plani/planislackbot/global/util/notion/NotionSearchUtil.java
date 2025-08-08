package com.chatbot.plani.planislackbot.global.util.notion;

import java.util.Collection;

/**
 * Notion 검색 관련 유틸
 */
public class NotionSearchUtil {

    public NotionSearchUtil() {}

    /**
     * 검색 결과가 비어있는지 확인
     */
    public static boolean isEmpty(Collection<?> results) {
        return results == null || results.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> results) {
        return results != null && !results.isEmpty();
    }
}
