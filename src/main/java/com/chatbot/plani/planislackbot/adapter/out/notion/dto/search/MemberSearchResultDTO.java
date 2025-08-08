package com.chatbot.plani.planislackbot.adapter.out.notion.dto.search;

public record MemberSearchResultDTO(
        String pageId,
        String url,
        String name,
        String team,
        String role,
        String email,
        String contact,
        String extension,
        String joinDate
) {}
