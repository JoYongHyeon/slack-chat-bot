package com.chatbot.plani.planislackbot.application.service.slack;

import com.chatbot.plani.planislackbot.adapter.in.web.slack.dto.SlackBlockActionDTO;
import com.chatbot.plani.planislackbot.adapter.in.web.slack.dto.SlackEventCallbackDTO;
import com.chatbot.plani.planislackbot.adapter.in.web.slack.dto.SlackViewSubmissionDTO;
import com.chatbot.plani.planislackbot.application.dispatcher.NotionCommandDispatcher;
import com.chatbot.plani.planislackbot.application.port.in.BotCommand;
import com.chatbot.plani.planislackbot.domain.notion.enums.NotionDbIntent;
import com.chatbot.plani.planislackbot.domain.slack.enums.ServiceIntent;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@RequiredArgsConstructor
@Description("Slack 에서 Notion 으로 라우팅하는 1차 진입점")
public class SlackToNotionServiceImpl implements BotCommand {

    private final NotionCommandDispatcher notionCommandDispatcher;


    @Override
    public ServiceIntent getServiceIntent() {
        return ServiceIntent.NOTION;
    }

    @Override
    public void handleEvent(SlackEventCallbackDTO slackEvent, String subIntent) {
        NotionDbIntent dbIntent = NotionDbIntent.fromString(subIntent);
        notionCommandDispatcher.dispatch(slackEvent, dbIntent);
    }

    @Override
    public void interaction(SlackBlockActionDTO slackAction, String actionId) {
        notionCommandDispatcher.dispatchInteraction(slackAction, actionId);
    }

    @Override
    public void viewSubmission(SlackViewSubmissionDTO slackCallback, String callbackId) {
        notionCommandDispatcher.dispatchViewSubmission(slackCallback, callbackId);
    }

    @Override
    public ResponseEntity<String> slash(Map<String, String> params) {
        return null;
    }
}
