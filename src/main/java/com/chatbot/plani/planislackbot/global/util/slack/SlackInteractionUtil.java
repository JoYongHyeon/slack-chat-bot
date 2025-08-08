package com.chatbot.plani.planislackbot.global.util.slack;

import com.chatbot.plani.planislackbot.adapter.in.web.slack.dto.SlackBlockActionDTO;

import java.util.Optional;

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
     * Slack 인터랙션 payload에서 채널ID 추출
     * - 해당 액션이 발생한 Slack 채널의 ID
     *
     * @param dto SlackBlockActionDTO (슬랙 인터랙션 payload)
     * @return 채널 ID (없으면 null)
     */
    public static String extractChannel(SlackBlockActionDTO dto) {
        return Optional.ofNullable(dto.channel())
                .map(SlackBlockActionDTO.Channel::id)
                .orElse(null);
    }
}
