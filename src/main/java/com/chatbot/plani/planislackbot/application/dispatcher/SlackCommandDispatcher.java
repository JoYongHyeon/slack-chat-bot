package com.chatbot.plani.planislackbot.application.dispatcher;

import com.chatbot.plani.planislackbot.adapter.in.web.slack.dto.SlackBlockActionDTO;
import com.chatbot.plani.planislackbot.adapter.in.web.slack.dto.SlackEventCallbackDTO;
import com.chatbot.plani.planislackbot.application.port.in.BotCommand;
import com.chatbot.plani.planislackbot.application.port.in.NotionInteractionHandler;
import com.chatbot.plani.planislackbot.application.port.out.openai.keyword.KeywordExtractionPort;
import com.chatbot.plani.planislackbot.application.service.slack.handler.interaction.MeetingSummaryInteractionHandler;
import com.chatbot.plani.planislackbot.domain.slack.enums.ServiceIntent;
import com.chatbot.plani.planislackbot.global.dto.IntentResultDTO;
import com.chatbot.plani.planislackbot.global.util.JsonUtil;
import com.chatbot.plani.planislackbot.global.util.LogTimerUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executor;

import static com.chatbot.plani.planislackbot.global.util.constant.CommonConstant.ERROR_UNSUPPORTED_REQUEST;

/**
 * Slack 이벤트를 1차 intent에 따라 BotCommand 구현체로 분배하는 클래스
 * <p>
 * - OpenAI를 통해 1차/2차 intent를 추출
 * - 해당 intent에 맞는 BotCommand를 찾아 비동기로 위임
 * - Slack에는 즉시 OK 응답, 후속 응답은 핸들러가 직접 처리
 */
@Service
public class SlackCommandDispatcher {

    private final Map<ServiceIntent, BotCommand> serviceMap;
    private final KeywordExtractionPort keywordExtractionPort;
    private final Executor slackToNotionExecutor;
    private final ObjectMapper objectMapper;
    private final Map<String, NotionInteractionHandler> interactionHandlerMap;

    public SlackCommandDispatcher(
            Map<ServiceIntent, BotCommand> serviceMap,
            KeywordExtractionPort keywordExtractionPort,
            Executor slackToNotionExecutor,
            ObjectMapper objectMapper,
            @Qualifier("interactionHandlerMap")
            Map<String, NotionInteractionHandler> interactionHandlerMap
    ) {
        this.serviceMap = serviceMap;
        this.keywordExtractionPort = keywordExtractionPort;
        this.slackToNotionExecutor = slackToNotionExecutor;
        this.objectMapper = objectMapper;
        this.interactionHandlerMap = interactionHandlerMap;
    }

    /**
     * 1차 intent(서비스)별 분기/위임 처리
     */
    public ResponseEntity<String> route(SlackEventCallbackDTO slackEvent) {

        // 1. 슬랙 메시지에서 텍스트 추출
        String text = Optional.ofNullable(slackEvent.event())
                .map(SlackEventCallbackDTO.Event::text)
                .orElse("");

        // 2. OpenAI 로 1,2차 Intent 동시 추출 (JSON 반환)
        IntentResultDTO intentResultDTO = keywordExtractionPort.extractServiceIntent(text);
        ServiceIntent serviceIntent = ServiceIntent.fromString(intentResultDTO.service());

        // 3. 서비스 intent로 구현체 찾아서 실제 처리 위임
        BotCommand handler = serviceMap.get(serviceIntent);

        if (handler != null) {
            // 슬랙은 즉시 OK 응답, 실제 처리는 비동기로 실행
            slackToNotionExecutor.execute(() ->
                    LogTimerUtil.runWithTiming("슬랙 이벤트",
                        () -> handler.handleEvent(slackEvent, intentResultDTO.intent())));
        }

        return ResponseEntity.ok(ERROR_UNSUPPORTED_REQUEST);
    }

    /**
     * Slack 상호작용(버튼 클릭 등) 이벤트를 처리
     * <p>
     * 1. payload(JSON)를 DTO로 변환
     * 2. action_id 추출
     * 3. action_id에 해당하는 핸들러 조회
     * 4. 핸들러가 있으면 비동기로 실행(즉시 응답)
     */
    public ResponseEntity<String> routeInteraction(String payload) {

        // 1. payload → DTO 파싱
        SlackBlockActionDTO dto = JsonUtil.toObject(payload, SlackBlockActionDTO.class, objectMapper);

        // 2. action_id 추출
        String actionId = Optional.ofNullable(dto)
                .map(d -> d.actions().getFirst())
                .map(SlackBlockActionDTO.Action::actionId)
                .orElse(null);

        // 3. action_id에 맞는 핸들러 조회
        NotionInteractionHandler handler = interactionHandlerMap.get(actionId);

        if (handler != null) {
            slackToNotionExecutor.execute(() ->
                    LogTimerUtil.runWithTiming("슬랙 인터랙션",
                        () -> handler.handleInteraction(dto)));
        }

        return ResponseEntity.ok(ERROR_UNSUPPORTED_REQUEST);
    }

    /**
     * Slack 슬래시 커맨드(/search 등) 분기 처리
     */
    public ResponseEntity<String> routeCommand(Map<String, String> params) {
        return null;
    }
}
