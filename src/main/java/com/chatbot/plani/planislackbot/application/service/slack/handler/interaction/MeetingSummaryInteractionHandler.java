package com.chatbot.plani.planislackbot.application.service.slack.handler.interaction;

import com.chatbot.plani.planislackbot.adapter.in.web.slack.dto.SlackBlockActionDTO;
import com.chatbot.plani.planislackbot.application.port.in.NotionInteractionHandler;
import com.chatbot.plani.planislackbot.application.port.out.openai.summary.OpenAiNotionPageSummaryPort;
import com.chatbot.plani.planislackbot.application.port.out.slack.SlackSendPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.chatbot.plani.planislackbot.global.util.constant.slack.SlackConstant.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MeetingSummaryInteractionHandler implements NotionInteractionHandler {

    private final OpenAiNotionPageSummaryPort openAiNotionPageSummaryPort;
    private final SlackSendPort slackSendPort;

    @Override
    public String getActionId() {
        return SUMMARIZE_ACTION_ID;
    }

    @Override
    public void handleInteraction(SlackBlockActionDTO dto) {
        // 1. payload(JSON)를 파싱해서 pageId 추출
        String pageId = Optional.ofNullable(dto.actions())
                .filter(actions -> !actions.isEmpty())
                .map(actions -> actions.getFirst().value())
                .orElse(null);

        String channel = Optional.ofNullable(dto.channel())
                .map(SlackBlockActionDTO.Channel::id)
                .orElse(null);

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
