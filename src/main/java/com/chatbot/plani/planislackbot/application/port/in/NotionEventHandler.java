package com.chatbot.plani.planislackbot.application.port.in;

import com.chatbot.plani.planislackbot.domain.notion.enums.NotionDbIntent;
import com.chatbot.plani.planislackbot.domain.slack.vo.SlackCommandVO;

/**
 * Notion의 intent 별 처리 핸들러 인터페이스
 *
 * - meeting, document, vacation, member 등 intent에 따라 분기
 * - 요청을 처리하고 응답은 Slack으로 직접 전송
 */
public interface NotionEventHandler {
    NotionDbIntent getDbIntent();

    /**
     * Slack 요청 처리 (예: 회의 검색) 및 직접 응답 전송
     */
    void handle(SlackCommandVO commandVO);
}
