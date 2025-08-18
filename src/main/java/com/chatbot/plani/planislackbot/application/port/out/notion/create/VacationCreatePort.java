package com.chatbot.plani.planislackbot.application.port.out.notion.create;


import java.util.Map;

public interface VacationCreatePort {

    /**
     * 휴가 페이지를 생성하고, 성공 시 생성된 페이지의 URL(또는 ID)을 반환한다.
     * 실패 시 null 반환.
     */
    void vacationCreate(String callbackId, Map<String, String>  formData);
}
