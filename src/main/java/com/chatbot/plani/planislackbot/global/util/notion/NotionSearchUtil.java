package com.chatbot.plani.planislackbot.global.util.notion;

import com.chatbot.plani.planislackbot.adapter.out.notion.NotionSearchResultDTO;

import java.util.List;

/**
 * Notion 검색 관련 유틸
 */
public class NotionSearchUtil {

    public NotionSearchUtil() {}

    /**
     * 검색 결과가 비어있는지 확인
     */
    public static boolean isEmpty(List<NotionSearchResultDTO> results) {
        return results == null || results.isEmpty();
    }
}
