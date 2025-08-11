package com.chatbot.plani.planislackbot.adapter.out.notion.dto.search;

public record VacationSearchResultDTO(
        String pageId,
        String url,
        String applicantName,
        String vacationType,
        String startDate,
        String endDate,
        String status,
        String approverName,
        String reason
) {}
