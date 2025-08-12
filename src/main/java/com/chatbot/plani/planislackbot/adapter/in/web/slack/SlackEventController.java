package com.chatbot.plani.planislackbot.adapter.in.web.slack;

import com.chatbot.plani.planislackbot.adapter.in.web.slack.dto.SlackEventCallbackDTO;
import com.chatbot.plani.planislackbot.application.dispatcher.SlackCommandDispatcher;
import com.chatbot.plani.planislackbot.global.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Slack 이벤트/인터랙션/커맨드를 수신하는 컨트롤러
 *
 * - /events   : Slack 이벤트 수신
 * - /interact : 버튼 등 인터랙션
 * - /command  : 슬래시 명령 처리
 *
 * 모든 요청은 Dispatcher에 위임하며, Dispatcher는 비동기로 핸들러에 위임
 */
@RestController
@RequestMapping("/slack")
@RequiredArgsConstructor
public class SlackEventController {

    private final SlackCommandDispatcher slackDispatcher;

    /**
     * Slack에서 이벤트를 수신해 1차 분배기로 전달
     */
    @PostMapping("/events")
    public ResponseEntity<String> onEvent(@RequestBody SlackEventCallbackDTO slackEvent) {
        // Slack Challenge 인증 요청 시 바로 응답 (최초 한번)
        if (StringUtil.isNotEmpty(slackEvent.challenge())) return  ResponseEntity.ok(slackEvent.challenge());
        return slackDispatcher.route(slackEvent);
    }

    /**
     * 2. 슬랙 Interaction - 버튼 클릭 등 인터랙션 처리
     */
    @PostMapping("/interact")
    public ResponseEntity<String> onInteract(@RequestParam("payload") String payload) {
        return slackDispatcher.routeInteraction(payload);
    }

    /**
     * 3. 슬랙 Slash Command - /명령어 요청 처리
     */
    @PostMapping("/command")
    public ResponseEntity<String> command(@RequestParam Map<String, String> params) {
        return slackDispatcher.routeSlash(params);
    }
}
