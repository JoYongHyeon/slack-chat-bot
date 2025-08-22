package com.chatbot.plani.planislackbot.application.service.slack.handler.interaction;

import com.chatbot.plani.planislackbot.adapter.in.web.slack.dto.SlackBlockActionDTO;
import com.chatbot.plani.planislackbot.application.port.in.NotionInteractionHandler;
import com.chatbot.plani.planislackbot.application.port.out.openai.summary.OpenAiNotionPageSummaryPort;
import com.chatbot.plani.planislackbot.application.port.out.slack.SlackSendPort;
import com.chatbot.plani.planislackbot.global.util.slack.SlackInteractionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import static com.chatbot.plani.planislackbot.global.util.constant.slack.SlackConstant.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class MeetingSummaryInteractionHandler implements NotionInteractionHandler {

    // TODO: 공통 예외처리 필요

    private final OpenAiNotionPageSummaryPort openAiNotionPageSummaryPort;
    private final SlackSendPort slackSendPort;

    @Override
    public String getActionId() {
        return SUMMARIZE_ACTION_ID;
    }

    @Override
    public void handleInteraction(SlackBlockActionDTO dto) {

        String pageId = SlackInteractionUtil.extractPageId(dto);

        String channel = SlackInteractionUtil.extractChannel(dto);

        if (pageId == null) {
            slackSendPort.sendText(channel, ERROR_SEND_SUMMARIZE);
        }

        if (channel == null) {
            log.warn("[Slack 채널 ID] : null  메시지 내용: {}", ERROR_UNKNOWN_CHANEL);
        }

        String summary = openAiNotionPageSummaryPort.summarizePage(pageId);
        slackSendPort.sendText(channel, summary);

    }
}
