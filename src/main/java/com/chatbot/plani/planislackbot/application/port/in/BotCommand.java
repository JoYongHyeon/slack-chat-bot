package com.chatbot.plani.planislackbot.application.port.in;

import com.chatbot.plani.planislackbot.adapter.in.web.slack.dto.SlackEventCallbackDTO;
import com.chatbot.plani.planislackbot.domain.slack.enums.ServiceIntent;
import jdk.jfr.Description;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * Slack 이벤트 처리용 서비스 인터페이스 (1차 intent 기준)
 *
 * - 예: notion, calendar 등 서비스 단위로 분기
 * - 모든 응답은 슬랙으로 직접 전송하며, 반환값은 사용하지 않음
 */
public interface BotCommand {

    /**
     * 본인이 담당하는 1차 intent(enum)를 반환 (예: ServiceIntent.NOTION)
     */
    ServiceIntent getServiceIntent();

    /**
     * Slack 이벤트를 처리하고 응답은 내부에서 직접 전송함 (비동기)
     * (2차 intent도 함께 전달)
     */
    void handleEvent(SlackEventCallbackDTO slackEvent, String subIntent);

    /**
     * 슬랙 인터랙션(버튼 클릭 등) 처리
     * @param slackEvent 인터랙션 DTO
     */
    ResponseEntity<String> interaction(SlackEventCallbackDTO slackEvent);

    /**
     * 커맨드(슬래시 명령 등) 처리
     * @param params 커맨드 파라미터 맵
     */
    ResponseEntity<String> command(Map<String, String> params);
}
