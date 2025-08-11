package com.chatbot.plani.planislackbot.adapter.out.notion.search;


import com.chatbot.plani.planislackbot.adapter.out.notion.base.AbstractEventSearchAdapter;
import com.chatbot.plani.planislackbot.adapter.out.notion.dto.search.VacationSearchResultDTO;
import com.chatbot.plani.planislackbot.application.port.out.notion.search.VacationSearchPort;
import com.chatbot.plani.planislackbot.application.port.out.openai.keyword.VacationKeywordExtractionPort;
import com.chatbot.plani.planislackbot.global.util.notion.NotionPageUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import notion.api.v1.NotionClient;
import notion.api.v1.model.databases.query.filter.PropertyFilter;
import notion.api.v1.model.pages.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.chatbot.plani.planislackbot.global.util.constant.notion.NotionConstant.*;

@Component
public class VacationEventSearchAdapter
        extends AbstractEventSearchAdapter<VacationSearchResultDTO, VacationKeywordExtractionPort>
        implements VacationSearchPort {

    public VacationEventSearchAdapter(NotionClient notionClient,
                                      VacationKeywordExtractionPort vacationKeywordExtractionPort,
                                      ObjectMapper objectMapper) {
        super(notionClient, vacationKeywordExtractionPort, objectMapper);
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
    protected VacationSearchResultDTO mapPageToDto(Page page) {
        // NotionPageUtil 로 안전하게 각 프로퍼티 추출
        return new VacationSearchResultDTO(
                page.getId(),
                page.getUrl(),
                NotionPageUtil.getPropertyText(page, VACATION_APPLICANT_NAME),
                NotionPageUtil.getPropertyText(page, VACATION_TYPE),
                NotionPageUtil.getPropertyText(page, VACATION_START_DATE),
                NotionPageUtil.getPropertyText(page, VACATION_END_DATE),
                NotionPageUtil.getPropertyText(page, VACATION_STATUS),
                NotionPageUtil.getPropertyText(page, VACATION_APPROVER_NAME),
                NotionPageUtil.getPropertyText(page, VACATION_REASON)
        );
    }
}
