package com.chatbot.plani.planislackbot.application.port.out.slack;

import com.chatbot.plani.planislackbot.domain.notion.enums.NotionDbIntent;
import com.slack.api.model.block.LayoutBlock;

import java.util.Collection;
import java.util.List;

/**
 * Slack 메시지 전송용 포트 – 어댑터에서 직접 사용
 */
public interface SlackSendPort {

    /**
     * Slack 채널에 일반 텍스트 메시지 전송
     *
     * @param channel 대상 Slack 채널 ID
     * @param message 메시지 본문
     */
    void sendText(String channel, String message);

    /**
     * Slack 채널에 노션 검색 결과(리스트)를 블록 메시지로 전송
     * @param channel Slack 채널 ID
     */
    void sendBlocks(String channel, List<LayoutBlock> blocks, String message);

    /**
     * Notion 검색 결과(DTO 리스트)를 intent별로 적절한 Slack 블록 메시지로 변환해 채널로 전송합니다.
     * <p>
     * - intent(MEETING, MEMBER 등)에 따라 각기 다른 메시지 레이아웃을 적용합니다.
     * - 실패/미지원 intent 시 에러 메시지를 대신 전송합니다.
     * </p>
     *
     * @param channel         Slack 채널 ID
     * @param notionDbIntent  Notion 검색 타입 (예: MEETING, MEMBER)
     * @param results         검색 결과 DTO 컬렉션
     */
    void sendNotionSearchResult(String channel, NotionDbIntent notionDbIntent, Collection<?> results);

}
