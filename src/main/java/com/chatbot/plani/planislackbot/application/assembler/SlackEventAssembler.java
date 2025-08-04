package com.chatbot.plani.planislackbot.application.assembler;

import com.chatbot.plani.planislackbot.adapter.in.web.slack.dto.SlackEventCallbackDTO;
import com.chatbot.plani.planislackbot.domain.slack.vo.SlackCommandVO;

import static com.chatbot.plani.planislackbot.adapter.in.web.slack.dto.SlackEventCallbackDTO.*;
import static com.chatbot.plani.planislackbot.global.util.StringUtil.*;

public class SlackEventAssembler {

    public static SlackCommandVO fromSlackEvent(SlackEventCallbackDTO slackEvent) {
        if (slackEvent == null || slackEvent.event() == null) return null;
        Event event = slackEvent.event();
        String text = event.text();
        String channel = event.channel();

        if (isEmpty(channel)) return null;

        // 멘션 제거한 원문 키워드 ("" or null일 수 있음) -> 안내 메시지를 위해 따로 처리
        String keyword = isEmpty(text) ? "" : removeSlackMentionPrefix(text);
        return new SlackCommandVO(channel, keyword);
    }
}
