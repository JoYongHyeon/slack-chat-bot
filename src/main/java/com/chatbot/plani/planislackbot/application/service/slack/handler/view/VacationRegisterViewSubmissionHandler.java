package com.chatbot.plani.planislackbot.application.service.slack.handler.view;

import com.chatbot.plani.planislackbot.adapter.in.web.slack.dto.SlackViewSubmissionDTO;
import com.chatbot.plani.planislackbot.application.port.in.NotionViewSubmissionHandler;
import com.chatbot.plani.planislackbot.application.port.out.notion.create.VacationCreatePort;
import com.chatbot.plani.planislackbot.global.util.slack.SlackInteractionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.chatbot.plani.planislackbot.global.util.constant.slack.SlackConstant.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class VacationRegisterViewSubmissionHandler implements NotionViewSubmissionHandler {

    private final VacationCreatePort vacationCreatePort;
    @Override
    public String getCallbackId() {
        return VACATION_REGISTER_MODAL_ID;
    }

    @Override
    public void handleViewSubmission(SlackViewSubmissionDTO dto) {

        String channel = SlackInteractionUtil.extractChannel(dto);

        if (channel == null) {
            log.warn("[Slack 채널 ID] : null  메시지 내용: {}", ERROR_UNKNOWN_CHANEL);
        }

        // 1. 폼 데이터 추출
        Map<String, String> formData = SlackInteractionUtil.extractFormData(dto);
        vacationCreatePort.vacationCreate(getCallbackId(), formData);

        // 2. 값 검증
//        if (formData.values().stream().anyMatch(v -> v == null || v.isBlank())) {
//            slackSendPort.sendText(channel, "❌ 모든 항목은 필수입니다. 다시 입력해주세요.");
//            return;
//        }
    }
}
