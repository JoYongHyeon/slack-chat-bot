package com.chatbot.plani.planislackbot.global.util.notion;

import notion.api.v1.model.databases.query.filter.PropertyFilter;
import notion.api.v1.model.databases.query.filter.condition.DateFilter;
import notion.api.v1.model.databases.query.filter.condition.MultiSelectFilter;
import notion.api.v1.model.databases.query.filter.condition.SelectFilter;
import notion.api.v1.model.databases.query.filter.condition.TextFilter;

import java.util.Arrays;
import java.util.List;

import static com.chatbot.plani.planislackbot.global.util.constant.notion.NotionConstant.*;


/**
 * Notion 필터 객체 생성 유틸리티
 * - 컬럼 타입별로 적절한 PropertyFilter 리스트를 반환한다.
 * - 가독성과 유지보수성을 최우선으로 설계
 */
public class NotionFilterUtil {
    public NotionFilterUtil() {}

    /**
     * 컬럼명과 값에 따라 적절한 Notion PropertyFilter 리스트를 생성
     *
     * @param property Notion 데이터베이스 컬럼명
     * @param value    검색 값 (날짜, 텍스트, 선택 등)
     */
    public static List<PropertyFilter> buildMeetingFilters(String property, String value) {

        return switch (property) {
            case MEETING_DATE      -> buildDateFilters(value);
            case MEETING_CATEGORY,
                 MEETING_STATUS,
                 MEETING_PLACE,
                 MEETING_HOST      -> buildSelectFilters(property, value);
            case MEETING_ATTENDEES -> buildMultiSelectFilters(property, value);
            case MEETING_TITLE,
                 MEETING_TIME      -> buildTextFilters(property, value);
            default -> List.of();
        };
    }

    /**
     * 컬럼명과 값에 따라 적절한 Notion PropertyFilter 리스트를 생성
     *
     * @param property Notion 데이터베이스 컬럼명
     * @param value    검색 값 (날짜, 텍스트, 선택 등)
     */
    public static List<PropertyFilter> buildMemberFilters(String property, String value) {
        return switch (property) {
            case MEMBER_NAME,
                 MEMBER_EMAIL,
                 MEMBER_CONTACT,
                 MEMBER_EXTENSION -> buildTextFilters(property, value);
            case MEMBER_TEAM,
                 MEMBER_ROLE      -> buildSelectFilters(property, value);
            case MEMBER_JOIN_DATE -> buildDateFilters(value);
            default -> List.of();
        };
    }

    public static List<PropertyFilter> buildDocumentFilters(String property, String value) {
        return switch (property) {
            case DOCUMENT_FILE_NAME,
                 DOCUMENT_DESCRIPTION -> buildTextFilters(property, value);
            case DOCUMENT_PROJECT,
                 DOCUMENT_CATEGORY,
                 DOCUMENT_UPLOADER,
                 DOCUMENT_STATUS      -> buildSelectFilters(property, value);
            case DOCUMENT_UPLOAD_DATE -> buildDateFilters(value);
            default -> List.of();
        };
    }

    /**
     * 날짜 필터 (범위: "~" 구분, 단일: equals)
     */
    private static List<PropertyFilter> buildDateFilters(String value) {

        if (value.contains("~")) {
            String[] dates = value.split("~");

            // 범위: onOrAfter(시작) ~ onOrBefore(종료)
            return Arrays.asList(
                    // 시작일 조건
                    dateFilterOnOrAfter(dates[0].trim()),
                    // 종료일 조건
                    dateFilterOnBefore(dates[1].trim())
            );

        } else {
            // 단일 날짜: equals 조건
            return List.of(dateFilterEquals(value.trim()));
        }
    }

    /**
     * 단일 선택(Select) 필터
     */
    private static List<PropertyFilter> buildSelectFilters(String property, String value) {
        PropertyFilter filter = new PropertyFilter();
        filter.setProperty(property);
        SelectFilter select = new SelectFilter();
        select.setEquals(value);
        filter.setSelect(select);
        return List.of(filter);
    }

    /**
     * 다중 선택(MultiSelect) 필터
     */
    private static List<PropertyFilter> buildMultiSelectFilters(String property, String value) {
        PropertyFilter filter = new PropertyFilter();
        filter.setProperty(property);
        MultiSelectFilter multi = new MultiSelectFilter();
        multi.setContains(value); // 여러 값 지원하려면 split 해서 contains 여러개로 추가
        filter.setMultiSelect(multi);
        return List.of(filter);
    }

    /**
     * 텍스트 포함(RichText) 필터
     */
    private static List<PropertyFilter> buildTextFilters(String property, String value) {
        PropertyFilter filter = new PropertyFilter();
        filter.setProperty(property);
        TextFilter text = new TextFilter();
        text.setContains(value);
        filter.setRichText(text);
        return List.of(filter);
    }

    /**
     * ========== 날짜 조건 세부 생성 메서드 =============================================
     * "날짜" 속성이 특정 날짜 이후(포함)인 PropertyFilter 생성
     *
     * @param date 기준 시작일(YYYY-MM-DD)
     * @return Notion PropertyFilter (onOrAfter)
     */
    private static PropertyFilter dateFilterOnOrAfter(String date) {
        PropertyFilter filter = new PropertyFilter();
        filter.setProperty(MEETING_DATE);
        DateFilter dateFilter = new DateFilter();
        dateFilter.setOnOrAfter(date);
        filter.setDate(dateFilter);
        return filter;
    }

    /**
     * "날짜" 속성이 특정 날짜 이전(포함)인 PropertyFilter 생성
     *
     * @param date 기준 종료일(YYYY-MM-DD)
     * @return Notion PropertyFilter (onOrBefore)
     */
    private static PropertyFilter dateFilterOnBefore(String date) {
        PropertyFilter filter = new PropertyFilter();
        filter.setProperty(MEETING_DATE);
        DateFilter dateFilter = new DateFilter();
        dateFilter.setOnOrBefore(date);
        filter.setDate(dateFilter);
        return filter;
    }

    /**
     * "날짜" 속성이 특정 날짜와 정확히 일치하는 PropertyFilter 생성
     *
     * @param date 일치시킬 날짜(YYYY-MM-DD)
     * @return Notion PropertyFilter (equals)
     */
    private static PropertyFilter dateFilterEquals(String date) {
        PropertyFilter filter = new PropertyFilter();
        filter.setProperty(MEETING_DATE);
        DateFilter dateFilter = new DateFilter();
        dateFilter.setEquals(date);
        filter.setDate(dateFilter);
        return filter;
    }
    // ========== 날짜 조건 세부 생성 메서드 =============================================
}
