package com.chatbot.plani.planislackbot.application.port.out.notion.search;

import com.chatbot.plani.planislackbot.adapter.out.notion.dto.MeetingSearchResultDTO;

import java.util.List;

/**
 * [Port] 회의록 DB용 검색 포트 – 각 어댑터가 DB별로 구현
 */
public interface MeetingSearchPort {

    /**
     * 주어진 키워드와 DB Intent, Database ID를 기반으로
     * Notion 회의록 DB를 검색하여 결과 목록을 반환합니다.
     *
     * @param keyword    검색할 키워드(예: 회의명, 카테고리, 참석자 등 자연어 명령)
     * @param databaseId 검색 대상 Notion Database의 ID
     * @return           검색 결과 MeetingSearchResultDTO 리스트 (결과 없으면 빈 리스트)
     */
    List<MeetingSearchResultDTO> search(String keyword, String databaseId);

}
