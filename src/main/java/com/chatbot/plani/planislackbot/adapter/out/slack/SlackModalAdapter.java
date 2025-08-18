package com.chatbot.plani.planislackbot.adapter.out.slack;

import com.chatbot.plani.planislackbot.application.port.out.slack.SlackModalPort;
import com.slack.api.Slack;
import com.slack.api.model.view.View;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * - Slack SDK를 사용해 실제로 views.open API를 호출
 * - 인프라 세부사항(토큰/SDK)은 이 레이어에 캡슐화.
 */
@Component
public class SlackModalAdapter implements SlackModalPort {

    @Value("${slack.bot-token}")
    private String botToken;

    @Override
    public void openModal(String triggerId, View view) {

        try {
            Slack.getInstance().methods(botToken)
                    .viewsOpen(req -> req
                            .triggerId(triggerId)
                            .view(view));
        } catch (Exception ignore) {
            // TODO: 공통예외처리 필요
        }
    }
}
