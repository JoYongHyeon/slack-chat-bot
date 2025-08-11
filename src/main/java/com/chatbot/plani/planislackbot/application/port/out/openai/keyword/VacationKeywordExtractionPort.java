package com.chatbot.plani.planislackbot.application.port.out.openai.keyword;

import notion.api.v1.model.databases.query.filter.PropertyFilter;

import java.util.List;
import java.util.Map;

/**
 * 멤버 자연어 → Notion 쿼리 변환 및 필터 생성용 포트
 */
public interface VacationKeywordExtractionPort {

    String extractNotionQuery(String text);


    List<PropertyFilter> buildFilters(Map<String, String> conditions);
}
