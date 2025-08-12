package com.chatbot.plani.planislackbot.application.service.slack.handler.event;

import com.chatbot.plani.planislackbot.application.port.in.NotionEventHandler;
import com.chatbot.plani.planislackbot.domain.notion.enums.NotionDbIntent;
import com.chatbot.plani.planislackbot.domain.slack.vo.SlackCommandVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VacationRegisterEventHandler implements NotionEventHandler {

    @Override
    public NotionDbIntent getDbIntent() {
        return NotionDbIntent.VACATION_REGISTER;
    }

    @Override
    public void handle(SlackCommandVO commandVO) {

    }
}
