package com.chatbot.plani.planislackbot.global.util.notion;

import notion.api.v1.model.common.RichTextType;
import notion.api.v1.model.databases.DatabaseProperty;
import notion.api.v1.model.pages.Page;
import notion.api.v1.model.pages.PageProperty;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.chatbot.plani.planislackbot.global.util.constant.notion.NotionConstant.ERROR_NOTION_TITLE;
import static com.chatbot.plani.planislackbot.global.util.constant.notion.NotionConstant.MEETING_TITLE;

/**
 * Notion Page 데이터 안전 추출 유틸리티.
 */
public class NotionPageUtil {
    private NotionPageUtil() {
    }

    /**
     * Notion Page에서 "제목" 추출.
     * (회의록, 문서 등 공통)
     */
    public static String extractTitleFromPage(Page page) {
        return Optional.of(page.getProperties())
                .map(props ->
                        props.getOrDefault(MEETING_TITLE, props.getOrDefault("title", null)))
                .map(PageProperty::getTitle)
                .filter(list -> !list.isEmpty())
                .map(list -> list.getFirst().getPlainText())
                .orElse(ERROR_NOTION_TITLE);
    }


    /**
     * Notion Page에서 특정 프로퍼티의 텍스트 안전 추출.
     *
     * @param page         Notion Page
     * @param propertyName 추출할 프로퍼티명
     *
     * - Text/Title: 텍스트 추출
     * - Select: 선택값 추출
     * - MultiSelect: 콤마 구분 문자열로 추출
     * - Date: YYYY-MM-DD 형식 문자열로 추출
     *
     * @return 텍스트, 없으면 "-"
     */
    public static String getPropertyText(Page page, String propertyName) {
        PageProperty property = page.getProperties().get(propertyName);
        if (property == null) return "-";

        // 1. Title/RichText(텍스트 계열)
        if (property.getTitle() != null && !property.getTitle().isEmpty()) {
            return property.getTitle().getFirst().getPlainText();
        }
        if (property.getRichText() != null && !property.getRichText().isEmpty()) {
            return property.getRichText().getFirst().getPlainText();
        }

        // 2. Select
        if (property.getSelect() != null && property.getSelect().getName() != null) {
            return property.getSelect().getName();
        }

        // 3. MultiSelect
        if (property.getMultiSelect() != null && !property.getMultiSelect().isEmpty()) {
            // 여러 값 콤마로 이어 붙임
            return property.getMultiSelect().stream()
                    .map(DatabaseProperty.MultiSelect.Option::getName)
                    .filter(Objects::nonNull)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("-");
        }

        // 4. Date
        if (property.getDate() != null && property.getDate().getStart() != null) {
            String start = property.getDate().getStart();
            String end   = property.getDate().getEnd();

            if (end != null && !end.equals(start)) {
                return start + " ~ " + end;
            }
            return start;
        }

        // 5. Number, Email, PhoneNumber 등
        if (property.getNumber() != null) {
            return property.getNumber().toString();
        }
        if (property.getEmail() != null) {
            return property.getEmail();
        }
        if (property.getPhoneNumber() != null) {
            return property.getPhoneNumber();
        }

        return "-";
    }

    /* ==========================
       생성 관련 메서드 (새로 추가)
       ========================== */

    /**
     * Notion의 Title 속성 값을 생성합니다.
     * - 반드시 DB의 "타이틀 속성명" 키에만 넣어야 합니다. (대개 "Name" 또는 커스텀명)
     */
    public static PageProperty titleProperty(String text) {
        PageProperty p = new PageProperty();
        p.setTitle(List.of(new PageProperty.RichText(
                RichTextType.Text,
                new PageProperty.RichText.Text(text),
                null, text, null, null, null
        )));
        return p;
    }

    /**
     * Notion의 Select 속성 값을 생성합니다.
     * - optionName은 해당 DB에 "미리 존재하는 옵션명"과 정확히 일치해야 합니다.
     *   (예: 유형=연차/반차, 상태=신청/승인 등)
     */
    public static PageProperty selectProperty(String optionName) {
        PageProperty p = new PageProperty();
        p.setSelect(new DatabaseProperty.Select.Option(null, optionName, null, null));
        return p;
    }

    /**
     * Notion의 Date 속성(단일 날짜 컬럼) 값을 생성합니다.
     * - 시작일 컬럼처럼 "start"만 채울 때 사용합니다.
     * - 예: "2025-08-19" (ISO 8601 yyyy-MM-dd 또는 ISO datetime 문자열)
     */
    public static PageProperty dateStartProperty(String startDate) {
        PageProperty p = new PageProperty();
        p.setDate(new PageProperty.Date(startDate, null, null));
        return p;
    }

    /**
     * Notion의 Date 속성(기간 컬럼) 값을 생성합니다.
     * - 하나의 컬럼이 start~end 기간을 표현할 때 사용합니다.
     * - 예: 기간(휴가일자) 컬럼이 start~end를 동시에 가지는 경우.
     */
    public static PageProperty dateRangeProperty(String startDate, String endDate) {
        PageProperty p = new PageProperty();
        p.setDate(new PageProperty.Date(startDate, endDate, null)); // start+end 설정
        return p;
    }

    /**
     * Notion의 Rich Text 속성 값을 생성합니다.
     * - 메모/사유 등 자유 텍스트에 사용합니다.
     */
    public static PageProperty richTextProperty(String text) {
        PageProperty p = new PageProperty();
        p.setRichText(List.of(new PageProperty.RichText(
                RichTextType.Text,
                new PageProperty.RichText.Text(text),
                null, text, null, null, null
        )));
        return p;
    }
}
