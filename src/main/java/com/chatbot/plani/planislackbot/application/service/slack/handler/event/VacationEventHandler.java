package com.chatbot.plani.planislackbot.application.service.slack.handler.event;


import com.chatbot.plani.planislackbot.adapter.out.notion.dto.search.VacationSearchResultDTO;
import com.chatbot.plani.planislackbot.application.port.in.NotionEventHandler;
import com.chatbot.plani.planislackbot.application.port.out.notion.search.VacationSearchPort;
import com.chatbot.plani.planislackbot.application.port.out.slack.SlackSendPort;
import com.chatbot.plani.planislackbot.domain.notion.enums.NotionDbIntent;
import com.chatbot.plani.planislackbot.domain.slack.vo.SlackCommandVO;
import com.chatbot.plani.planislackbot.global.config.notion.NotionDatabaseProperties;
import com.chatbot.plani.planislackbot.global.util.notion.helper.NotionEventHandlerHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VacationEventHandler implements NotionEventHandler {

    private final VacationSearchPort vacationSearchPort;
    private final SlackSendPort slackSendPort;
    private final NotionDatabaseProperties notionDatabaseProperties;
    private final NotionEventHandlerHelper notionEventHandlerHelper;

    @Override
    public NotionDbIntent getDbIntent() {
        return NotionDbIntent.VACATION;
    }

    @Override
    public void handle(SlackCommandVO commandVO) {

        // 1. 명령 / 키워드 / 검색결과 체크는 helper 에서 담당 (문제시 return)
        if (notionEventHandlerHelper.invalidCommand(commandVO, this.getClass().getName())) return;
        if (notionEventHandlerHelper.emptyKeyword(commandVO, slackSendPort)) return;

        // 2. 휴가 DB ID 를 설정에서 바로 꺼내 전달
        String dbId = notionDatabaseProperties.vacationId();

        // 3. Notion 페이지 검색
        List<VacationSearchResultDTO> searchResults = vacationSearchPort.search(commandVO.keyword(), dbId);
        if (notionEventHandlerHelper.emptySearchResult(searchResults, slackSendPort, commandVO.channel())) return;

        // 4. 검색 결과 전송
        slackSendPort.sendNotionSearchResult(commandVO.channel(), getDbIntent(), searchResults);
    }
}
