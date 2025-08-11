package com.chatbot.plani.planislackbot.global.util.constant.notion;

public class NotionConstant {

    // 공통
    public static final String ERROR_NOTION_TITLE = "제목없음";
    public static final String ERROR_DATABASE_ID = "지원하지 않는 데이터베이스 ID 입니다.";

    // Notion 내에서 사용하는 필드명 상수 모음
    public static final String NOTION_TITLE = "title";

    // --- 1. 회의록 DB (이미 작성된 부분 참고) ---
    public static final String MEETING_TITLE      = "회의명";
    public static final String MEETING_DATE       = "날짜";
    public static final String MEETING_TIME       = "시간";
    public static final String MEETING_PLACE      = "장소";
    public static final String MEETING_CATEGORY   = "카테고리";
    public static final String MEETING_STATUS     = "상태";
    public static final String MEETING_ATTENDEES  = "참석자";
    public static final String MEETING_HOST       = "진행자";

    // --- 2. 멤버 DB ---
    public static final String MEMBER_NAME        = "이름";
    public static final String MEMBER_TEAM        = "소속";
    public static final String MEMBER_ROLE        = "직책";
    public static final String MEMBER_EMAIL       = "이메일";
    public static final String MEMBER_CONTACT     = "연락처";
    public static final String MEMBER_EXTENSION   = "내선 번호";
    public static final String MEMBER_JOIN_DATE   = "입사일";

    // --- 3. 문서 DB ---
    public static final String DOCUMENT_FILE_NAME    = "파일명";
    public static final String DOCUMENT_PROJECT      = "프로젝트";
    public static final String DOCUMENT_FILE         = "파일";
    public static final String DOCUMENT_CATEGORY     = "카테고리";
    public static final String DOCUMENT_UPLOAD_DATE  = "업로드일";
    public static final String DOCUMENT_UPLOADER     = "업로더";
    public static final String DOCUMENT_STATUS       = "상태";
    public static final String DOCUMENT_DESCRIPTION  = "설명";

    // --- 4. 휴가 관리 DB ---
    public static final String VACATION_APPLICANT_NAME = "신청자";
    public static final String VACATION_TYPE           = "유형";
    public static final String VACATION_START_DATE     = "시작일";
    public static final String VACATION_END_DATE       = "종료일";
    public static final String VACATION_STATUS         = "상태";
    public static final String VACATION_APPROVER_NAME  = "담당자";
    public static final String VACATION_REASON         = "사유";

    // 문서 DB

    // 휴가 DB
}
