package com.chatbot.plani.planislackbot.global.config.slack;

import com.chatbot.plani.planislackbot.application.port.in.BotCommand;
import com.chatbot.plani.planislackbot.application.port.in.NotionEventHandler;
import com.chatbot.plani.planislackbot.application.port.in.NotionInteractionHandler;
import com.chatbot.plani.planislackbot.application.port.in.NotionViewSubmissionHandler;
import com.chatbot.plani.planislackbot.domain.notion.enums.NotionDbIntent;
import com.chatbot.plani.planislackbot.domain.slack.enums.ServiceIntent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 핸들러 자동 맵핑(Spring DI 기반)
 */
@Configuration
public class HandlerMapConfig {

    /**
     * 1차 intent(ServiceIntent) → BotCommand 구현체 맵핑
     * ex) ServiceIntent.NOTION → SlackToNotionServiceImpl
     */
    @Bean
    public Map<ServiceIntent, BotCommand> serviceMap(List<BotCommand> commands) {
        return commands.stream()
                .collect(Collectors.toMap(
                        BotCommand::getServiceIntent,
                        Function.identity()
                ));
    }

    /**
     * 2차 intent(NotionDbIntent) → NotionEventHandler 구현체 맵핑
     * ex) NotionDbIntent.MEETING → MeetingEventHandler
     */
    @Bean
    public Map<NotionDbIntent, NotionEventHandler> handlerMap(List<NotionEventHandler> handlers) {
        return handlers.stream()
                .collect(Collectors.toMap(
                        NotionEventHandler::getDbIntent,
                        Function.identity()
                ));
    }

    /**
     * actionId(String) → NotionInteractionHandler 구현체 맵핑
     * ex) "summarize_page" → MeetingSummaryInteractionHandler
     * 슬랙 인터랙션(버튼 등) 처리 전용
     */
    @Bean
    public Map<String, NotionInteractionHandler> interactionHandlerMap(List<NotionInteractionHandler> handlers) {
        return handlers.stream()
                .collect(Collectors.toMap(
                        NotionInteractionHandler::getActionId,
                        Function.identity()
                ));
    }

    /**
     * callbackId(String) → NotionViewSubmissionHandler 구현체 매핑
     * ex) "vacation_register_modal" → VacationRegisterViewSubmissionHandler
     * Slack view_submission(모달 제출) 처리 전용
     */
    @Bean
    public Map<String, NotionViewSubmissionHandler> viewSubmissionHandlerMap(List<NotionViewSubmissionHandler> handlers) {
        return handlers.stream()
                .collect(Collectors.toMap(
                        NotionViewSubmissionHandler::getCallbackId,
                        Function.identity()
                ));
    }
}
