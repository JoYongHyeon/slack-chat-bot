package com.chatbot.plani.planislackbot.application.port.out.openai.summary;

public interface OpenAiNotionPageSummaryPort {

    /**
     * Notion 페이지 전체 내용 요약
     */
    String summarizePage(String text);
}
