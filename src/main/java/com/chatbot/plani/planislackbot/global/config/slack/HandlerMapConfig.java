package com.chatbot.plani.planislackbot.global.config.slack;

import com.chatbot.plani.planislackbot.application.port.in.BotCommand;
import com.chatbot.plani.planislackbot.application.port.in.NotionEventHandler;
import com.chatbot.plani.planislackbot.application.port.in.NotionInteractionHandler;
import com.chatbot.plani.planislackbot.application.port.out.openai.keyword.KeywordExtractionPort;
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
     * KeywordExtractionPort 구현체 맵 (DB별 분기용)
     *
     * - DB별로 서로 다른 프롬프트/파싱 전략이 필요한 경우,
     *   각 DatabaseId(예: meetingId, memberId 등)를 key로 하여
     *   해당 Database에 맞는 KeywordExtractionPort 구현체를 선택적으로 사용.
     *
     * - NotionSearchAdapter 등에서 databaseId에 따라
     *   알맞은 KeywordExtractionPort를 선택해 자연어 → 쿼리 변환을 위임할 때 사용.
     *
     * 예) keywordExtractionPortMap.get(meetingId).extractNotionQuery(...)
     */
    @Bean
    public Map<String, KeywordExtractionPort> keywordExtractionPortMap(List<KeywordExtractionPort> ports) {
        return ports.stream()
                .collect(Collectors.toMap(
                        KeywordExtractionPort::getDatabaseId,
                        Function.identity()
                ));
    }
}
