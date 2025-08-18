package com.chatbot.plani.planislackbot.adapter.out.notion.create;

import com.chatbot.plani.planislackbot.application.port.out.notion.create.VacationCreatePort;
import com.chatbot.plani.planislackbot.global.config.notion.NotionDatabaseProperties;
import com.chatbot.plani.planislackbot.global.util.notion.NotionPageUtil;
import lombok.RequiredArgsConstructor;
import notion.api.v1.NotionClient;
import notion.api.v1.model.common.RichTextType;
import notion.api.v1.model.databases.DatabaseProperty;
import notion.api.v1.model.pages.PageParent;
import notion.api.v1.model.pages.PageParentType;
import notion.api.v1.model.pages.PageProperty;
import notion.api.v1.request.pages.CreatePageRequest;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.chatbot.plani.planislackbot.global.util.constant.notion.NotionConstant.*;


@Component
@RequiredArgsConstructor
public class VacationCreateAdapter implements VacationCreatePort {

    private final NotionClient notionClient;
    private final NotionDatabaseProperties notionDatabaseProperties;

    @Override
    public void vacationCreate(String callbackId, Map<String, String> formData) {
        String applicant = formData.get(VACATION_APPLICANT_NAME_FORM);  // 예: "신청자"
        String type      = formData.get(VACATION_TYPE_FORM);            // 예: "오후반차"
        String startDate = formData.get(VACATION_START_DATE_FORM);      // 예: "2025-08-18"
        String endDate   = formData.get(VACATION_END_DATE_FORM);        // 예: "2025-08-18"
        String statusVal = formData.get(VACATION_STATUS_FORM); // 상태 옵션 예시
        String approver  = formData.get(VACATION_APPROVER_NAME_FORM);
        String reason    = formData.get(VACATION_REASON_FORM);

        Map<String, PageProperty> properties = new HashMap<>();

        // 1. 신청자 (Title)
        properties.put(VACATION_APPLICANT_NAME, NotionPageUtil.titleProperty(applicant));

        // 2. 유형 (Select) - (DB에 사전 등록된 옵션명과 동일해야 함)
        properties.put(VACATION_TYPE, NotionPageUtil.selectProperty(type));

        // 3. 시작일 (Date)
        properties.put(VACATION_START_DATE, NotionPageUtil.dateStartProperty(startDate));

        // 4. 종료일 (Date) - (단일 날짜, 값 있을 때만)
        properties.put(VACATION_END_DATE, NotionPageUtil.dateStartProperty(endDate));

        // 5. 상태 (Select)
        properties.put(VACATION_STATUS, NotionPageUtil.selectProperty(statusVal));

        // 6. 담당자 (Rich Text)
        properties.put(VACATION_APPROVER_NAME, NotionPageUtil.selectProperty(approver));

        // 7. 사유 (Rich Text)
        properties.put(VACATION_REASON, NotionPageUtil.richTextProperty(reason));

        // Notion 페이지 생성 요청
        CreatePageRequest req = new CreatePageRequest(
                new PageParent(
                        PageParentType.DatabaseId,
                        notionDatabaseProperties.vacationId(),
                        null, null),
                properties
        );

        notionClient.createPage(req);
    }

}
