package com.chatbot.plani.planislackbot.application.port.out.slack;

import com.chatbot.plani.planislackbot.adapter.out.notion.NotionSearchResultDTO;

import java.util.List;

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
     * @param results 노션 검색 결과 목록
     */
    void sendBlocks(String channel, List<NotionSearchResultDTO> results);


    /**
     * 기존 메시지 업데이트
     * @param channel 채널 ID
     * @param ts 메시지 타임스탬프
     * @param text 새로운 텍스트
     */
    void updateText(String channel, String ts, String text);
}
