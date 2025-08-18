package com.chatbot.plani.planislackbot.application.port.in;

import com.chatbot.plani.planislackbot.adapter.in.web.slack.dto.SlackBlockActionDTO;
import com.chatbot.plani.planislackbot.adapter.in.web.slack.dto.SlackEventCallbackDTO;
import com.chatbot.plani.planislackbot.adapter.in.web.slack.dto.SlackViewSubmissionDTO;
import com.chatbot.plani.planislackbot.domain.slack.enums.ServiceIntent;
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
     */
    /**
     *
     * @param slackAction -> 슬랙에서 받은 값
     * @param actionId -> notion:summarize_page 의 두번 째 값 (actionId 값)
     */
    void interaction(SlackBlockActionDTO slackAction, String actionId);

    /**
     * 모달 제출(View Submission) 이벤트 처리
     *
     * @param dto 모달 제출 payload
     */

    void viewSubmission(SlackViewSubmissionDTO dto, String callbackId);
    /**
     * 커맨드(슬래시 명령 등) 처리
     * @param params 커맨드 파라미터 맵
     */
    ResponseEntity<String> slash(Map<String, String> params);
}
