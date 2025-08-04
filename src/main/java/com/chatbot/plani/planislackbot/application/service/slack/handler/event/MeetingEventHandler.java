package com.chatbot.plani.planislackbot.application.service.slack.handler.event;

import com.chatbot.plani.planislackbot.adapter.out.notion.NotionSearchResultDTO;
import com.chatbot.plani.planislackbot.application.port.in.NotionEventHandler;
import com.chatbot.plani.planislackbot.application.port.out.notion.NotionSearchPort;
import com.chatbot.plani.planislackbot.application.port.out.slack.SlackSendPort;
import com.chatbot.plani.planislackbot.domain.notion.enums.NotionDbIntent;
import com.chatbot.plani.planislackbot.domain.slack.vo.SlackCommandVO;
import com.chatbot.plani.planislackbot.global.config.notion.NotionDatabaseProperties;
import com.chatbot.plani.planislackbot.global.util.StringUtil;
import com.chatbot.plani.planislackbot.global.util.notion.NotionSearchUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.chatbot.plani.planislackbot.global.util.constant.slack.SlackConstant.*;

/**
 * [Notion::회의록] 명령어 핸들러
 *
 * - 슬랙에서 받은 명령을 기반으로 Notion 회의록 DB를 검색
 * - 검색 결과를 Slack 블록 메시지로 전송
 * - 처리 실패 또는 키워드 누락 시 에러 메시지 전송
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MeetingEventHandler implements NotionEventHandler {

    private final NotionSearchPort notionSearchPort;
    private final SlackSendPort slackSendPort;
    private final NotionDatabaseProperties notionDatabaseProperties;

    @Override
    public NotionDbIntent getDbIntent() {
        return NotionDbIntent.MEETING;
    }

    @Override
    public void handle(SlackCommandVO commandVO) {
        if (commandVO == null) {
            // TODO: 모든 로그 공통 처리 필요
            log.error("[{}] : {}", ERROR_UNKNOWN_CHANEL, this.getClass().getName());
            return;
        }

        // 1. 빈 키워드 처리
        if (StringUtil.isEmpty(commandVO.keyword())) {
            slackSendPort.sendText(commandVO.channel(), ERROR_KEYWORD);
            return;
        }

        // 2. 회의록 DB ID 를 설정에서 바로 꺼내 전달
        String dbId = notionDatabaseProperties.meetingId();

        // 3. Notion 페이지 검색
        List<NotionSearchResultDTO> searchResults = notionSearchPort.search(commandVO.keyword(), dbId);
        if (NotionSearchUtil.isEmpty(searchResults)) {
            slackSendPort.sendText(commandVO.channel(), ERROR_NO_SUCH_NOTION_PAGE);
            return;
        }

        // 4. 검색 결과 전송
        slackSendPort.sendBlocks(commandVO.channel(), searchResults);
    }
}
