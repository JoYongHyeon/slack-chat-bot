package com.chatbot.plani.planislackbot.adapter.out.notion.search;

import com.chatbot.plani.planislackbot.adapter.out.notion.base.AbstractEventSearchAdapter;
import com.chatbot.plani.planislackbot.adapter.out.notion.dto.search.DocumentSearchResultDTO;
import com.chatbot.plani.planislackbot.application.port.out.notion.search.DocumentSearchPort;
import com.chatbot.plani.planislackbot.application.port.out.openai.keyword.DocumentKeywordExtractionPort;
import com.chatbot.plani.planislackbot.global.util.notion.NotionPageUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import notion.api.v1.NotionClient;
import notion.api.v1.model.databases.query.filter.PropertyFilter;
import notion.api.v1.model.pages.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.chatbot.plani.planislackbot.global.util.constant.notion.NotionConstant.*;

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
        return extractionPort.buildFilters(conditions);
    }

    @Override
    protected DocumentSearchResultDTO mapPageToDto(Page page) {
        return new DocumentSearchResultDTO(
                page.getId(),
                page.getUrl(),
                NotionPageUtil.getPropertyText(page, DOCUMENT_FILE_NAME),
                NotionPageUtil.getPropertyText(page, DOCUMENT_PROJECT),
                NotionPageUtil.getPropertyText(page, DOCUMENT_CATEGORY),
                NotionPageUtil.getPropertyText(page, DOCUMENT_UPLOAD_DATE),
                NotionPageUtil.getPropertyText(page, DOCUMENT_UPLOADER),
                NotionPageUtil.getPropertyText(page, DOCUMENT_STATUS),
                NotionPageUtil.getPropertyText(page, DOCUMENT_DESCRIPTION)
        );
    }
}
