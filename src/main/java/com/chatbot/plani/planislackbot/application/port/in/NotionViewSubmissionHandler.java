package com.chatbot.plani.planislackbot.application.port.in;

import com.chatbot.plani.planislackbot.adapter.in.web.slack.dto.SlackViewSubmissionDTO;

/**
 * Slack view_submission callbackId 기반 핸들러
 */
public interface NotionViewSubmissionHandler {

    /**
     * 이 핸들러가 담당하는 callbackId
     */
    String getCallbackId();

    /**
     * view_submission 이벤트 처리
     */
    void handleViewSubmission(SlackViewSubmissionDTO dto);
}
