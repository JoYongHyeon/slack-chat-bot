package com.chatbot.plani.planislackbot.domain.notion.enums;

import jdk.jfr.Description;
import lombok.Getter;

@Description("노션 2차 분기 처리")
public enum NotionDbIntent {

    // 회의록
    MEETING,
    // 멤버
    MEMBER,
    // 문서
    DOCUMENT,
    // 휴가
    VACATION,
    // 그 외
    UNKNOWN;

    public static NotionDbIntent fromString(String value) {
        try {
            return NotionDbIntent.valueOf(value.toUpperCase());
        } catch (Exception e) {
            return UNKNOWN;
        }
    }
}
