package com.chatbot.plani.planislackbot.application.port.out.notion.search;

import com.chatbot.plani.planislackbot.adapter.out.notion.dto.DocumentSearchResultDTO;

import java.util.List;

/**
 * [Port] Notion 멤버 DB 검색 포트
 */
public interface DocumentSearchPort {

    /**
     * 주어진 키워드와 DB ID로 Document 문서 DB 검색.
     *
     * @param keyword    검색 키워드(자연어)
     * @param databaseId Notion Database ID
     * @return           검색 결과 리스트 (없으면 빈 리스트)
     */
    List<DocumentSearchResultDTO> search(String keyword, String databaseId);
}
