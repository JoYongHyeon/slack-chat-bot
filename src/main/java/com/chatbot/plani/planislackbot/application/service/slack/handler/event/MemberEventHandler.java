package com.chatbot.plani.planislackbot.application.service.slack.handler.event;

import com.chatbot.plani.planislackbot.adapter.out.notion.dto.search.MemberSearchResultDTO;
import com.chatbot.plani.planislackbot.application.port.in.NotionEventHandler;
import com.chatbot.plani.planislackbot.application.port.out.notion.search.MemberSearchPort;
import com.chatbot.plani.planislackbot.application.port.out.slack.SlackSendPort;
import com.chatbot.plani.planislackbot.domain.notion.enums.NotionDbIntent;
import com.chatbot.plani.planislackbot.domain.slack.vo.SlackCommandVO;
import com.chatbot.plani.planislackbot.global.config.notion.NotionDatabaseProperties;
import com.chatbot.plani.planislackbot.global.util.notion.helper.NotionEventHandlerHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * [Notion::회의록] 명령어 핸들러
 *
 * - 슬랙에서 전달받은 명령을 해석해 Notion 회의록 DB 검색
 * - 결과를 Slack 메시지(블록 UI)로 전송
 * - 입력 검증/에러는 헬퍼에서 처리
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberEventHandler implements NotionEventHandler {

    private final MemberSearchPort memberSearchPort;
    private final SlackSendPort slackSendPort;
    private final NotionDatabaseProperties notionDatabaseProperties;
    private final NotionEventHandlerHelper notionEventHandlerHelper;

    @Override
    public NotionDbIntent getDbIntent() {
        return NotionDbIntent.MEMBER;
    }

    @Override
    public void handle(SlackCommandVO commandVO) {

        // 1. 명령 / 키워드 / 검색결과 체크는 helper 에서 담당 (문제시 return)
        if (notionEventHandlerHelper.invalidCommand(commandVO, this.getClass().getName())) return;
        if (notionEventHandlerHelper.emptyKeyword(commandVO, slackSendPort)) return;

        // 2. 회의록 DB ID 를 설정에서 바로 꺼내 전달
        String dbId = notionDatabaseProperties.memberId();

        // 3. Notion 페이지 검색
        List<MemberSearchResultDTO> searchResults = memberSearchPort.search(commandVO.keyword(), dbId);
        if (notionEventHandlerHelper.emptySearchResult(searchResults, slackSendPort, commandVO.channel())) return;

        // 4. 검색 결과 전송
        slackSendPort.sendNotionSearchResult(commandVO.channel(), getDbIntent(), searchResults);
    }
}
