package com.chatbot.plani.planislackbot.global.util.notion;

import notion.api.v1.model.databases.DatabaseProperty;
import notion.api.v1.model.pages.Page;
import notion.api.v1.model.pages.PageProperty;

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
}
