package com.chatbot.plani.planislackbot.adapter.in.web.slack.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @see <a href="https://api.slack.com/apis/events-api">
 * Slack 이벤트 응답을 조회합니다.</a>
 */
public record SlackEventCallbackDTO(

        String token,
        String challenge,
        @JsonProperty("team_id") String teamId,
        @JsonProperty("api_app_id") String apiAppId,
        Event event,
        String type,
        @JsonProperty("event_id") String eventId,
        @JsonProperty("event_time") long eventTime,
        List<Authorization> authorizations,
        @JsonProperty("is_ext_shared_channel") boolean isExtSharedChannel,
        @JsonProperty("event_context") String eventContext
) {
    public record Event(
            String user,
            String type,
            String ts,
            @JsonProperty("client_msg_id") String clientMsgId,
            String text,
            String team,
            List<Block> blocks,
            String channel,
            @JsonProperty("event_ts") String eventTs
    ) {
        public record Block(
                String type,
                @JsonProperty("block_id") String blockId,
                List<Element> elements
        ) {
            public record Element(
                    String type,
                    List<SubElement> elements,
                    @JsonProperty("user_id") String userid,
                    String text
            ) {
                public record SubElement(
                        String type,
                        @JsonProperty("user_id") String userId,
                        String text
                ) {}
            }
        }
    }

    public record Authorization(
            @JsonProperty("enterprise_id") String enterpriseId,
            @JsonProperty("team_id") String teamId,
            @JsonProperty("user_id") String userId,
            @JsonProperty("is_bot") boolean isBot,
            @JsonProperty("is_enterprise_install") boolean isEnterpriseInstall
    ) {}
}
