package com.chatbot.plani.planislackbot.adapter.out.notion.base;

import com.chatbot.plani.planislackbot.global.util.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import notion.api.v1.NotionClient;
import notion.api.v1.model.databases.QueryResults;
import notion.api.v1.model.databases.query.filter.CompoundFilter;
import notion.api.v1.model.databases.query.filter.PropertyFilter;
import notion.api.v1.model.databases.query.filter.QueryTopLevelFilter;
import notion.api.v1.model.pages.Page;
import notion.api.v1.request.databases.QueryDatabaseRequest;

import java.util.List;
import java.util.Map;

/**
 * Notion DB 검색용 추상 어댑터.
 * - 공통 검색 템플릿 제공 (final)
 * - 도메인별 쿼리, 필터, DTO 매핑만 오버라이드
 *
 * @param <T> 반환 DTO 타입 (ex: MeetingSearchResultDTO)
 * @param <E> 키워드 추출/필터 빌더 Port 타입
 */
public abstract class AbstractEventSearchAdapter<T, E> {

    protected final NotionClient notionClient;
    protected final E extractionPort;
    protected final ObjectMapper objectMapper;

    public AbstractEventSearchAdapter(NotionClient notionClient,
                                      E extractionPort,
                                      ObjectMapper objectMapper) {
        this.notionClient = notionClient;
        this.extractionPort = extractionPort;
        this.objectMapper = objectMapper;
    }

    /**
     * Notion DB 검색 템플릿 메서드.
     * - 1. 자연어 → 쿼리(JSON)
     * - 2. 쿼리 JSON → 조건 Map
     * - 3. 조건 → 필터 빌드
     * - 4. 필터 조합
     * - 5. Notion API 호출
     * - 6. Page → DTO 매핑 후 결과 리스트 반환
     */
    public final List<T> search(String keyword, String databaseId) {
        String queryJson = extractQuery(keyword);
        Map<String, String> conditions = JsonUtil.toMap(queryJson, objectMapper);
        List<PropertyFilter> filters = buildFilters(conditions);

        QueryTopLevelFilter filterObject = buildQueryFilter(filters);

        QueryDatabaseRequest queryRequest = new QueryDatabaseRequest(
                databaseId,
                filterObject,
                null, null, null
        );

        QueryResults results = notionClient.queryDatabase(queryRequest);
        return results.getResults().stream()
                .map(this::mapPageToDto)
                .toList();
    }

    // AND/단일/전체 조합 처리
    private QueryTopLevelFilter buildQueryFilter(List<PropertyFilter> filters) {
        if (filters.size() > 1) {
            CompoundFilter andFilter = new CompoundFilter();
            andFilter.setAnd(filters);
            return andFilter;
        } else if (filters.size() == 1) {
            return filters.getFirst();
        }
        return null;
    }

    // --- 도메인별 구현 Hook ---

    /** 자연어 → Notion 쿼리(JSON) 변환 */
    protected abstract String extractQuery(String keyword);

    /** 조건 Map → Notion 필터 리스트 변환 */
    protected abstract List<PropertyFilter> buildFilters(Map<String, String> conditions);

    /** Notion Page → 도메인별 DTO 변환 */
    protected abstract T mapPageToDto(Page page);
}





