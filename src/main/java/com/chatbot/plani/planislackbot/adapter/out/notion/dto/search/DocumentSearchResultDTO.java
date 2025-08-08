package com.chatbot.plani.planislackbot.adapter.out.notion.dto.search;

public record DocumentSearchResultDTO(
        String pageId,
        String url,
        String fileName,
        String project,
        String category,
        String uploadDate,
        String uploader,
        String status,
        String description
) {
}
