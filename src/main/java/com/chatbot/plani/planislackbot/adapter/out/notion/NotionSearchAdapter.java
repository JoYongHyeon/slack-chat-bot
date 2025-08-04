package com.chatbot.plani.planislackbot.adapter.out.notion;

import com.chatbot.plani.planislackbot.application.port.out.openai.keyword.KeywordExtractionPort;
import com.chatbot.plani.planislackbot.application.port.out.notion.NotionSearchPort;
import com.chatbot.plani.planislackbot.global.util.JsonUtil;
import com.chatbot.plani.planislackbot.global.util.notion.NotionFilterUtil;
import com.chatbot.plani.planislackbot.global.util.notion.NotionPageUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import notion.api.v1.NotionClient;
import notion.api.v1.model.databases.QueryResults;
import notion.api.v1.model.databases.query.filter.CompoundFilter;
import notion.api.v1.model.databases.query.filter.PropertyFilter;
import notion.api.v1.model.databases.query.filter.QueryTopLevelFilter;
import notion.api.v1.request.databases.QueryDatabaseRequest;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
@RequiredArgsConstructor
public class NotionSearchAdapter implements NotionSearchPort {

    private final NotionClient notionClient;
    private final KeywordExtractionPort keywordExtractionPort;
    private final ObjectMapper objectMapper;

    @Override
    public List<NotionSearchResultDTO> search(String keyword, String databaseId) {

        // 1. OpenAI로 자연어를 Notion 쿼리(JSON)로 변환
        String queryJson = keywordExtractionPort.extractNotionQuery(keyword);
        Map<String, String> conditions = JsonUtil.toMap(queryJson, objectMapper);


        // 2. 조건별 Notion PropertyFilter 생성 (헬퍼 유틸 사용)
        List<PropertyFilter> filters = conditions.entrySet().stream()
                .flatMap(entry ->
                        NotionFilterUtil.buildPropertyFilters(entry.getKey(), entry.getValue()).stream())
                .toList();


        // 3. 필터 조합 (AND or 단일 or 전체)
        QueryTopLevelFilter filterObject = null;
        if (filters.size() > 1) {
            CompoundFilter andFilter = new CompoundFilter();
            andFilter.setAnd(filters);
            filterObject = andFilter;
        } else if (filters.size() == 1) {
            filterObject = filters.getFirst();
        }

        // 4. 쿼리 요청 객체 생성
        QueryDatabaseRequest queryRequest = new QueryDatabaseRequest(
                databaseId,
                filterObject,
                null, null, null
        );

        // 5. Notion API 로 검색 실행
        QueryResults results = notionClient.queryDatabase(queryRequest);


        // 6. Page -> DTO 변환
        return results.getResults().stream()
                .map(page -> new NotionSearchResultDTO(
                        page.getId(),
                        NotionPageUtil.extractTitleFromPage(page),
                        page.getUrl()))
                .toList();
    }
}
