package com.chatbot.plani.planislackbot.global.util.slack;

import com.chatbot.plani.planislackbot.adapter.in.web.slack.dto.SlackBlockActionDTO;
import com.chatbot.plani.planislackbot.adapter.in.web.slack.dto.SlackViewSubmissionDTO;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Slack 인터랙션(Payload) 파싱 유틸리티
 * - action_id, 채널ID, 페이지ID 등 슬랙 버튼/이벤트 payload에서 필요한 값을 추출
 */
public class SlackInteractionUtil {

    private SlackInteractionUtil() {
    }

    /**
     * Slack 인터랙션 payload에서 pageId 추출
     *
     * @param dto SlackBlockActionDTO (슬랙 인터랙션 payload)
     * @return pageId (없으면 null)
     */
    public static String extractPageId(SlackBlockActionDTO dto) {
        return Optional.ofNullable(dto.actions())
                .filter(actions -> !actions.isEmpty())
                .map(actions -> actions.getFirst().value())
                .orElse(null);
    }

    /**
     * Slack 액션(payload)에서 채널 ID 추출
     *
     * <p>버튼 클릭 / 메시지 액션(= block_actions) 이벤트용</p>
     * @param dto SlackBlockActionDTO (block_actions payload)
     * @return 채널 ID, 없으면 null
     */
    public static String extractChannel(SlackBlockActionDTO dto) {
        return Optional.ofNullable(dto.channel())
                .map(SlackBlockActionDTO.Channel::id)
                .orElse(null);
    }

    /**
     * Slack 모달(view_submission) payload에서 채널 ID 추출
     *
     * <p>채널 ID는 모달 생성 시 `private_metadata`에 심어서 전달해야 함.</p>
     *
     * @param dto SlackViewSubmissionDTO (view_submission payload)
     * @return 채널 ID, 없으면 null
     */
    public static String extractChannel(SlackViewSubmissionDTO dto) {
        return Optional.ofNullable(dto.view())
                .map(SlackViewSubmissionDTO.View::privateMetadata)
                .filter(s -> !s.isBlank())
                .orElse(null);
    }

    /**
     * actions 배열의 첫 번째 action_id를 반환 (없으면 null)
     */
    public static String extractActionId(SlackBlockActionDTO dto) {
        return Optional.ofNullable(dto)
                .filter(d -> d.actions() != null && !d.actions().isEmpty())
                .map(d -> d.actions().getFirst())
                .map(SlackBlockActionDTO.Action::actionId)
                .orElse(null);
    }

    /**
     * view 객체의 callback_id를 반환 (없으면 null)
     */
    public static String extractCallbackId(SlackViewSubmissionDTO dto) {
        return Optional.ofNullable(dto)
                .map(SlackViewSubmissionDTO::view)
                .map(SlackViewSubmissionDTO.View::callbackId)
                .orElse(null);
    }

    /**
     * view_submission → (action_id -> 값) 맵 추출
     */
    public static Map<String, String> extractFormData(SlackViewSubmissionDTO dto) {
        Map<String, Map<String, SlackViewSubmissionDTO.InputValue>> values = extractValueMap(dto);
        return flattenActionValues(values);
    }

    /**
     * state.values 안전 추출
     */
    private static Map<String, Map<String, SlackViewSubmissionDTO.InputValue>> extractValueMap(
            SlackViewSubmissionDTO dto) {

        return Optional.ofNullable(dto)
                .map(SlackViewSubmissionDTO::view)
                .map(SlackViewSubmissionDTO.View::state)
                .map(SlackViewSubmissionDTO.State::values)
                .orElse(Map.of());
    }

    /**
     * blockId/actionId 2단계 맵을 (actionId -> 값) 1단계 맵으로 평탄화
     */
    private static Map<String, String> flattenActionValues(
            Map<String, Map<String, SlackViewSubmissionDTO.InputValue>> values
    ) {
        return values.entrySet().stream()
                .flatMap(block ->
                        block.getValue().entrySet().stream()
                                .map(action -> Map.entry(
                                        action.getKey(), // action_id
                                        extractInputValue(action.getValue())
                                ))
                )
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /** 하나의 InputValue에서 실제 값을 뽑는다.
     *  - text input   → value
     *  - static_select→ selected_option.value
     *  - datepicker   → selected_date
     *  (필요 시 timepicker, users_select 등도 여기서 확장)
     */
    private static String extractInputValue(SlackViewSubmissionDTO.InputValue input) {
        if (input == null) return "";
        if (input.value() != null) return input.value();
        if (input.selectedOption() != null && input.selectedOption().value() != null)
            return input.selectedOption().value();
        if (input.selectedDate() != null) return input.selectedDate();
        return "";
    }
}
