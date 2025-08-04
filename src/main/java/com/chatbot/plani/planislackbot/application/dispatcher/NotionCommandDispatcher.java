package com.chatbot.plani.planislackbot.application.dispatcher;

import com.chatbot.plani.planislackbot.adapter.in.web.slack.dto.SlackEventCallbackDTO;
import com.chatbot.plani.planislackbot.application.assembler.SlackEventAssembler;
import com.chatbot.plani.planislackbot.application.port.in.NotionEventHandler;
import com.chatbot.plani.planislackbot.domain.notion.enums.NotionDbIntent;
import com.chatbot.plani.planislackbot.domain.slack.vo.SlackCommandVO;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Description("Notion intent (회의록/멤버/문서/휴가 등) 분배기")
public class NotionCommandDispatcher {

    private final Map<NotionDbIntent, NotionEventHandler> handlerMap;

    // Notion intent(회의, 휴가, 멤버, 문서 등) 핸들러로 2차 분기
    public void dispatch(SlackEventCallbackDTO slackEvent, NotionDbIntent dbIntent) {

        SlackCommandVO slackCommandVO = SlackEventAssembler.fromSlackEvent(slackEvent);
        NotionEventHandler handler = handlerMap.get(dbIntent);

        if (handler != null) {
            handler.handle(slackCommandVO);
        }
    }
}
