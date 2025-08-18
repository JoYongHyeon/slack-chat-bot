package com.chatbot.plani.planislackbot.adapter.out.notion.dto.create;

import lombok.Getter;

public record VacationCreateRequestDTO(

        String dbId,
        // 연차/오전반차/오후반차/병가/기타
        String type,
        // 신청자
        String applicant,
        String startDate,
        String endDate,
        // 신청/승인/거절
        String status,
        // 담당자
        String manager,
        // 사유
        String reason
) {
}
