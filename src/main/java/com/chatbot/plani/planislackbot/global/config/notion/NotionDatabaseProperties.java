package com.chatbot.plani.planislackbot.global.config.notion;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "notion.database")
public record NotionDatabaseProperties(
        String meetingId,
        String memberId,
        String documentId,
        String vacationId
) {}
