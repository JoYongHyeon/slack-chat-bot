package com.chatbot.plani.planislackbot.adapter.in.web.slack.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @see <a href="https://api.slack.com/interactivity/handling#payloads"
 * Slack Block Action(버튼, 메뉴 등) Interactive 이벤트 구조
 */
public record SlackBlockActionDTO(

        String type,
        User user,
        @JsonProperty("api_app_id") String apiAPppId,
        String token,
        Container container,
        @JsonProperty("trigger_id") String triggerId,
        Team team,
        Channel channel,
        Message message,
        List<Action> actions,
        @JsonProperty("response_url") String responseUrl
) {

    public record User(
            String id,
            String username,
            String name,
            @JsonProperty("team_id") String teamId
    ) {}

    public record Container(
            String type,
            @JsonProperty("message_ts") String messageTs,
            @JsonProperty("channel_id") String channelId,
            @JsonProperty("is_ephemeral") boolean isEphemeral
    ) {}

    public record Team(
            String id,
            String domain
    ) {}

    public record Channel(
            String id,
            String name
    ) {}

    public record Message(
            String user,
            String type,
            String ts,
            @JsonProperty("bot_id") String botId,
            @JsonProperty("app_id") String appId,
            String text,
            String team,
            List<Block> blocks
    ) {
        public record Block(
                String type,
                @JsonProperty("block_id") String blockId
                // text, accessory 등 필요시 추가
        ) {}
    }

    public record Action(
            @JsonProperty("action_id") String actionId,
            @JsonProperty("block_id") String blockId,
            String type,
            String value,
            @JsonProperty("actions_ts") String actionTs
            // text 등 필요시 추가
    ) {}
}





