package com.chatbot.plani.planislackbot.application.port.out.openai.keyword;

import notion.api.v1.model.databases.query.filter.PropertyFilter;

import java.util.List;
import java.util.Map;

/**
 * 문서 자연어 → Notion 쿼리 변환 및 필터 생성용 포트
 */
public interface DocumentKeywordExtractionPort {


    /**
     * 자연어 명령어(키워드)를 Notion 쿼리(JSON 문자열)로 변환합니다.
     *
     * @param text  사용자 입력 자연어 (예: "A프로젝트 최신 상태 자료 보여줘")
     * @return      Notion DB 검색 조건을 나타내는 JSON 문자열 (ex: {"프로젝트":"A프로젝트","상태":"최신"})
     */
    String extractNotionQuery(String text);


    /**
     * Notion 쿼리(JSON → Map) 정보를 바탕으로, Notion API용 PropertyFilter 리스트를 생성합니다.
     * (각 컬럼/타입별 필터 로직은 어댑터/유틸에서 구현)
     *
     * @param conditions  쿼리 JSON 파싱 결과(Map<String, String>)
     * @return            Notion API용 PropertyFilter 리스트
     */
    List<PropertyFilter> buildFilters(Map<String, String> conditions);
}
