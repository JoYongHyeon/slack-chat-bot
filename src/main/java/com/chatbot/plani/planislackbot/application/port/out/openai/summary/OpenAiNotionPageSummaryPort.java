package com.chatbot.plani.planislackbot.application.port.out.openai.summary;

/**
 * Notion 페이지 본문 요약용 포트 (OpenAI 활용)
 */
public interface OpenAiNotionPageSummaryPort {

    /**
     * Notion 페이지 전체 본문을 요약해서 반환합니다.
     *
     * @param text  요약할 Notion 페이지 전체 본문(plain text)
     * @return      요약 결과 문자열(예: 분류/항목별 핵심 내용)
     */
    String summarizePage(String text);
}
