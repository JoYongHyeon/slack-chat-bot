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

import static com.chatbot.plani.planislackbot.global.util.constant.notion.NotionConstant.ERROR_DATABASE_ID;

/**
 * - Notion DB에서 키워드 기반으로 페이지를 검색하는 어댑터
 * - DB ID별 KeywordExtractionPort를 동적으로 선택하여
 *   자연어 키워드를 Notion 쿼리(JSON)로 변환 후 검색 실행
 * - 검색 결과를 NotionSearchResultDTO 리스트로 반환
 */
@Component
@RequiredArgsConstructor
public class NotionSearchAdapter implements NotionSearchPort {

    private final NotionClient notionClient;
    // DB ID별 쿼리 추출 Port 맵 (동적 분기)
    private final Map<String, KeywordExtractionPort>  keywordExtractionPortMap;
    private final ObjectMapper objectMapper;

    @Override
    public List<NotionSearchResultDTO> search(String keyword, String databaseId) {

        // 1. DB ID에 맞는 쿼리 추출 Port 선택
        KeywordExtractionPort keywordExtractionPort = keywordExtractionPortMap.get(databaseId);
        if (keywordExtractionPort == null) {
            // TODO: 공통 예외 처리 필요
            throw new IllegalArgumentException("[" + databaseId + "]: " + ERROR_DATABASE_ID);
        }

        // 2. 자연어 명령어 → DB 구조에 맞는 Notion 쿼리(JSON) 생성
        String queryJson = keywordExtractionPort.extractNotionQuery(keyword);

        // 3. 쿼리 JSON → <컬럼명, 값> Map 변환
        Map<String, String> conditions = JsonUtil.toMap(queryJson, objectMapper);

        // 4. 쿼리 조건(Map) → DB 타입별 Notion PropertyFilter 리스트 생성
        //    (각 어댑터에서 DB 특성에 맞는 필터 빌더를 호출)
        List<PropertyFilter> filters = keywordExtractionPort.buildFilters(conditions);

        // 5. 필터 조합 (AND or 단일 or 전체)
        QueryTopLevelFilter filterObject = null;
        if (filters.size() > 1) {
            CompoundFilter andFilter = new CompoundFilter();
            andFilter.setAnd(filters);
            filterObject = andFilter;
        } else if (filters.size() == 1) {
            filterObject = filters.getFirst();
        }

        // 6. 쿼리 요청 객체 생성
        QueryDatabaseRequest queryRequest = new QueryDatabaseRequest(
                databaseId,
                filterObject,
                null, null, null
        );

        // 7. Notion API 로 검색 실행
        QueryResults results = notionClient.queryDatabase(queryRequest);


        // 8. Page -> DTO 변환
        return results.getResults().stream()
                .map(page -> new NotionSearchResultDTO(
                        page.getId(),
                        NotionPageUtil.extractTitleFromPage(page),
                        page.getUrl()))
                .toList();
    }
}
