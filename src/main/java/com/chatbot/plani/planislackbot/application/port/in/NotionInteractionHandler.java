package com.chatbot.plani.planislackbot.application.port.in;


import com.chatbot.plani.planislackbot.adapter.in.web.slack.dto.SlackBlockActionDTO;

/**
 * 슬랙 인터랙션(버튼 등) 별로 분기 처리하는 핸들러 인터페이스
 */
public interface NotionInteractionHandler {

    /**
     * 이 핸들러가 담당하는 actionId
     */
    String getActionId();

    /**
     * 인터랙션 처리 (비즈니스 로직 실행)
     */
    void handleInteraction(SlackBlockActionDTO dto);
}
