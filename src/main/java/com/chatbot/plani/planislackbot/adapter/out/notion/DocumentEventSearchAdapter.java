package com.chatbot.plani.planislackbot.adapter.out.notion;

import com.chatbot.plani.planislackbot.adapter.out.notion.base.AbstractEventSearchAdapter;
import com.chatbot.plani.planislackbot.adapter.out.notion.dto.DocumentSearchResultDTO;
import com.chatbot.plani.planislackbot.application.port.out.notion.search.DocumentSearchPort;
import com.chatbot.plani.planislackbot.application.port.out.openai.keyword.DocumentKeywordExtractionPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import notion.api.v1.NotionClient;
import notion.api.v1.model.databases.query.filter.PropertyFilter;
import notion.api.v1.model.pages.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * [Adapter] Notion 문서 DB 검색 어댑터.
 * - 자연어 키워드 → 쿼리/필터/Notion API → DTO 리스트
 */
@Component
public class DocumentEventSearchAdapter
        extends AbstractEventSearchAdapter<DocumentSearchResultDTO, DocumentKeywordExtractionPort>
        implements DocumentSearchPort {

    public DocumentEventSearchAdapter(NotionClient notionClient,
                                      DocumentKeywordExtractionPort documentKeywordExtractionPort,
                                      ObjectMapper objectMapper) {
        super(notionClient, documentKeywordExtractionPort, objectMapper);
    }

    @Override
    protected String extractQuery(String keyword) {
        return extractionPort.extractNotionQuery(keyword);
    }

    @Override
    protected List<PropertyFilter> buildFilters(Map<String, String> conditions) {
        return List.of();
    }

    @Override
    protected DocumentSearchResultDTO mapPageToDto(Page page) {
        return null;
    }
}
