package com.chatbot.plani.planislackbot.domain.slack.enums;


/**
 * 1차 intent(서비스 구분)
 */
public enum ServiceIntent {
    NOTION,
    UNKNOWN;

    public static ServiceIntent fromString(String value) {
        try {
            return ServiceIntent.valueOf(value.toUpperCase());
        } catch (Exception e) {
            return UNKNOWN;
        }
    }
}
