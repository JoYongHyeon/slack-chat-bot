package com.chatbot.plani.planislackbot.application.port.out.notion;

import com.chatbot.plani.planislackbot.adapter.out.notion.NotionSearchResultDTO;

import java.util.List;

public interface NotionSearchPort {

    /**
     * 주어진 키워드로 지정된 Notion 데이터베이스에서 페이지를 검색합니다.
     *
     * @param keyword    검색할 키워드 (예: 제목, 내용 등)
     * @param databaseId 검색 대상 Notion 데이터베이스 ID
     * @return           검색 결과(페이지) 목록
     */
    List<NotionSearchResultDTO> search(String keyword, String databaseId);

}
