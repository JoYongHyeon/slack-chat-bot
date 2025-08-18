package com.chatbot.plani.planislackbot.adapter.in.web.slack.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true) // top-level에서 모르는 필드 무시
public record SlackViewSubmissionDTO(
        String type,
        User user,
        Team team,
        @JsonProperty("api_app_id") String apiAppId,
        String token,
        @JsonProperty("trigger_id") String triggerId,
        View view,
        @JsonProperty("response_urls") Object responseUrls,
        @JsonProperty("is_enterprise_install") boolean isEnterpriseInstall,
        String enterprise
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record User(
            String id,
            String username,
            String name,
            @JsonProperty("team_id") String teamId
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Team(
            String id,
            String domain
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record View(
            String id,
            @JsonProperty("team_id") String teamId,
            String type,
            // title/submit/close 는 객체!
            TextObject title,
            TextObject submit,
            TextObject close,
            @JsonProperty("callback_id") String callbackId,
            State state,
            @JsonProperty("private_metadata") String privateMetadata,
            String hash,
            @JsonProperty("clear_on_close") boolean clearOnClose,
            @JsonProperty("notify_on_close") boolean notifyOnClose,
            @JsonProperty("root_view_id") String rootViewId,
            @JsonProperty("previous_view_id") String previousViewId,
            @JsonProperty("app_id") String appId,
            @JsonProperty("external_id") String externalId,
            @JsonProperty("app_installed_team_id") String appInstalledTeamId,
            @JsonProperty("bot_id") String botId
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record TextObject(
            String type,
            String text,
            Boolean emoji
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record State(
            Map<String, Map<String, InputValue>> values
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record InputValue(
            String type,
            String value,
            @JsonProperty("selected_option") SelectedOption selectedOption,
            @JsonProperty("selected_date") String selectedDate
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SelectedOption(
            OptionText text,
            String value
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record OptionText(
            String type,
            String text,
            Boolean emoji
    ) {}
}
