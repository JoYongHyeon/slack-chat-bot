package com.chatbot.plani.planislackbot.application.port.out.openai.keyword;


import notion.api.v1.model.databases.query.filter.PropertyFilter;

import java.util.List;
import java.util.Map;

/**
 * - 슬랙 등에서 입력된 자연어 명령을 각 DB에 맞는 쿼리(JSON) 또는 인텐트로 구조화
 * - DB 타입별 구현체(Adapter) 확장 가능 (OCP)
 */
public interface KeywordExtractionPort {

    /**
     * 이 포트 구현체가 담당하는 Notion DB ID 반환
     * (ex: 회의DB, 멤버DB 등)
     */
    String getDatabaseId();

    /**
     * 자연어 명령어를 Notion DB 쿼리(JSON)로 변환
     * (예: "7월 완료된 회의" → {"날짜":"2025-07-01~2025-07-31","상태":"완료"})
     */
    String extractNotionQuery(String text);

    /**
     * 쿼리(JSON 파싱 결과)에서 Notion API용 PropertyFilter 리스트 생성
     * - DB 컬럼/타입에 따라 각 구현체에서 적합한 필터 생성 로직을 제공
     *
     * @param conditions JSON 파싱된 필터 조건 Map
     * @return Notion API에 사용할 PropertyFilter 리스트
     */
    List<PropertyFilter> buildFilters(Map<String, String> conditions);
}
