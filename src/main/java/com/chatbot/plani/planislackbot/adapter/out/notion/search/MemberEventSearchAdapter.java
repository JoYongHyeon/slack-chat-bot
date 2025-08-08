package com.chatbot.plani.planislackbot.adapter.out.notion.search;

import com.chatbot.plani.planislackbot.adapter.out.notion.base.AbstractEventSearchAdapter;
import com.chatbot.plani.planislackbot.adapter.out.notion.dto.search.MemberSearchResultDTO;
import com.chatbot.plani.planislackbot.application.port.out.notion.search.MemberSearchPort;
import com.chatbot.plani.planislackbot.application.port.out.openai.keyword.MemberKeywordExtractionPort;
import com.chatbot.plani.planislackbot.global.util.notion.NotionPageUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import notion.api.v1.NotionClient;
import notion.api.v1.model.databases.query.filter.PropertyFilter;
import notion.api.v1.model.pages.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.chatbot.plani.planislackbot.global.util.constant.notion.NotionConstant.*;
import static com.chatbot.plani.planislackbot.global.util.constant.notion.NotionConstant.MEMBER_CONTACT;
import static com.chatbot.plani.planislackbot.global.util.constant.notion.NotionConstant.MEMBER_EMAIL;
import static com.chatbot.plani.planislackbot.global.util.constant.notion.NotionConstant.MEMBER_EXTENSION;
import static com.chatbot.plani.planislackbot.global.util.constant.notion.NotionConstant.MEMBER_JOIN_DATE;
import static com.chatbot.plani.planislackbot.global.util.constant.notion.NotionConstant.MEMBER_ROLE;

/**
 * [Adapter] Notion 멤버 DB 검색 어댑터.
 * - 자연어 키워드 → 쿼리/필터/Notion API → DTO 리스트
 */
@Component
public class MemberEventSearchAdapter
        extends AbstractEventSearchAdapter<MemberSearchResultDTO, MemberKeywordExtractionPort>
        implements MemberSearchPort {

    public MemberEventSearchAdapter(NotionClient notionClient,
                                    MemberKeywordExtractionPort memberKeywordExtractionPort,
                                    ObjectMapper objectMapper) {
        super(notionClient, memberKeywordExtractionPort, objectMapper);
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
    protected MemberSearchResultDTO mapPageToDto(Page page) {
        // NotionPageUtil로 안전하게 각 프로퍼티 추출
        return new MemberSearchResultDTO(
                page.getId(),
                page.getUrl(),
                NotionPageUtil.getPropertyText(page, MEMBER_NAME),
                NotionPageUtil.getPropertyText(page, MEMBER_TEAM),
                NotionPageUtil.getPropertyText(page, MEMBER_ROLE),
                NotionPageUtil.getPropertyText(page, MEMBER_EMAIL),
                NotionPageUtil.getPropertyText(page, MEMBER_CONTACT),
                NotionPageUtil.getPropertyText(page, MEMBER_EXTENSION),
                NotionPageUtil.getPropertyText(page, MEMBER_JOIN_DATE)
        );
    }
}
