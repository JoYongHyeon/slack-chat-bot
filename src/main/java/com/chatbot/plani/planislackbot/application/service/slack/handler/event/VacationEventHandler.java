package com.chatbot.plani.planislackbot.application.service.slack.handler.event;

import com.chatbot.plani.planislackbot.application.port.in.NotionEventHandler;
import com.chatbot.plani.planislackbot.domain.notion.enums.NotionDbIntent;
import com.chatbot.plani.planislackbot.domain.slack.vo.SlackCommandVO;

public class VacationEventHandler implements NotionEventHandler {
    @Override
    public NotionDbIntent getDbIntent() {
        return null;
    }

    @Override
    public void handle(SlackCommandVO commandVO) {
    }
}
