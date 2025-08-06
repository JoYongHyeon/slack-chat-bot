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


    void sendNotionSearchResult(String channel, NotionDbIntent notionDbIntent, Collection<?> results);

}
