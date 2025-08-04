package com.chatbot.plani.planislackbot.global.dto;

/**
 * OpenAI 등 외부 LLM에서 추출한 1차(service)·2차(intent) 의도를 전달하는 DTO.
 *
 * 예시 JSON:
 * {"service":"notion","intent":"meeting"}
 */
public record IntentResultDTO(
        String service,
        String intent
) {}
