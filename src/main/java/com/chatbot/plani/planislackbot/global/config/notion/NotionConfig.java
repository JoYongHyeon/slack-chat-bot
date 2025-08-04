package com.chatbot.plani.planislackbot.global.config.notion;

import notion.api.v1.NotionClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(NotionDatabaseProperties.class)
public class NotionConfig {

    /**
     * Notion API 호출용 클라이언트 Bean
     */
    @Bean
    public NotionClient notionClient(@Value("${notion.api-key}") String notionToken) {
        return new NotionClient(notionToken);
    }
}
