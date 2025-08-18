package com.chatbot.plani.planislackbot.application.service.slack.handler.interaction;

import com.chatbot.plani.planislackbot.adapter.in.web.slack.dto.SlackBlockActionDTO;
import com.chatbot.plani.planislackbot.application.port.in.NotionInteractionHandler;
import com.chatbot.plani.planislackbot.application.port.out.slack.SlackModalPort;
import com.chatbot.plani.planislackbot.application.port.out.slack.SlackSendPort;
import com.chatbot.plani.planislackbot.global.util.StringUtil;
import com.chatbot.plani.planislackbot.global.util.slack.SlackInteractionUtil;
import com.chatbot.plani.planislackbot.global.util.slack.SlackModalUtil;
import com.slack.api.methods.request.views.ViewsOpenRequest;
import com.slack.api.model.view.View;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.chatbot.plani.planislackbot.global.util.constant.slack.SlackConstant.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class VacationRegisterInteractionHandler implements NotionInteractionHandler {

    private final SlackModalPort slackModalPort;
    private final SlackSendPort slackSendPort;

    @Override
    public String getActionId() {
        return VACATION_REGISTER_ACTION_ID;
    }

    @Override
    public void handleInteraction(SlackBlockActionDTO dto) {

        String triggerId = dto.triggerId();

        String channel = SlackInteractionUtil.extractChannel(dto);

        if (StringUtil.isEmpty(triggerId)) {
            slackSendPort.sendText(channel, ERROR_KEYWORD);
            return;
        }

        if (channel == null) {
            log.warn("[Slack 채널 ID] : null  메시지 내용: {}", ERROR_UNKNOWN_CHANEL);
        }

        // 모달 열기
        slackModalPort.openModal(triggerId, SlackModalUtil.vacationRegisterModal());
    }
}
