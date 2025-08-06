package com.chatbot.plani.planislackbot.adapter.out.notion;

import com.chatbot.plani.planislackbot.adapter.out.notion.base.AbstractEventSearchAdapter;
import com.chatbot.plani.planislackbot.adapter.out.notion.dto.MeetingSearchResultDTO;
import com.chatbot.plani.planislackbot.application.port.out.notion.search.MeetingSearchPort;
import com.chatbot.plani.planislackbot.application.port.out.openai.keyword.MeetingKeywordExtractionPort;
import com.chatbot.plani.planislackbot.domain.notion.enums.NotionDbIntent;
import com.chatbot.plani.planislackbot.global.util.JsonUtil;
import com.chatbot.plani.planislackbot.global.util.notion.NotionPageUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import notion.api.v1.NotionClient;
import notion.api.v1.model.databases.QueryResults;
import notion.api.v1.model.databases.query.filter.CompoundFilter;
import notion.api.v1.model.databases.query.filter.PropertyFilter;
import notion.api.v1.model.databases.query.filter.QueryTopLevelFilter;
import notion.api.v1.model.pages.Page;
import notion.api.v1.model.pages.PageProperty;
import notion.api.v1.request.databases.QueryDatabaseRequest;
import org.springframework.stereotype.Component;

import java.util.*;


/**
 * [Adapter] Notion 회의록 DB 검색 어댑터.
 * - 자연어 키워드 → 쿼리/필터/Notion API → DTO 리스트
 */

@Component
public class MeetingEventSearchAdapter
        extends AbstractEventSearchAdapter<MeetingSearchResultDTO, MeetingKeywordExtractionPort>
        implements MeetingSearchPort {

    public MeetingEventSearchAdapter(NotionClient notionClient,
                                     MeetingKeywordExtractionPort meetingKeywordExtractionPort,
                                     ObjectMapper objectMapper) {
        super(notionClient, meetingKeywordExtractionPort, objectMapper);
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
    protected MeetingSearchResultDTO mapPageToDto(Page page) {
        // NotionPageUtil로 제목 추출
        return new MeetingSearchResultDTO(
                page.getId(),
                NotionPageUtil.extractTitleFromPage(page),
                page.getUrl()
        );
    }
}
