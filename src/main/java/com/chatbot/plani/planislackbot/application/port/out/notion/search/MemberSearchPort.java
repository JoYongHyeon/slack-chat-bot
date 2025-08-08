package com.chatbot.plani.planislackbot.application.port.out.notion.search;

import com.chatbot.plani.planislackbot.adapter.out.notion.dto.search.MemberSearchResultDTO;

import java.util.List;

/**
 * [Port] Notion 멤버 DB 검색 포트
 */
public interface MemberSearchPort {


    /**
     * 주어진 키워드와 DB ID로 Notion 멤버 DB 검색.
     *
     * @param keyword    검색 키워드(자연어)
     * @param databaseId Notion Database ID
     * @return           검색 결과 리스트 (없으면 빈 리스트)
     */
    List<MemberSearchResultDTO> search(String keyword, String databaseId);
}
