package com.chatbot.plani.planislackbot.global.util.constant.slack;

public class SlackConstant {

    /* =========================================
     * 1. Slack JSON Payload 필드명
     * ========================================= */
    public static final String ACTION = "actions";
    public static final String ACTION_ID = "action_id";
    public static final String VALUE = "value";
    public static final String CHANNEL = "channel";
    public static final String CHANNEL_ID = "channel_id";
    public static final String ID = "id";

    /* =========================================
     * 2. Slack action_id & modal_id
     *    (버튼 클릭, 모달 식별용)
     * ========================================= */
    public static final String SUMMARIZE_ACTION_ID = "notion:summarize_page";
    public static final String DOCUMENT_DOWNLOAD_ACTION_ID = "notion:document_download";
    public static final String VACATION_REGISTER_ACTION_ID = "notion:vacation_open_modal";
    public static final String VACATION_REGISTER_MODAL_ID = "notion:vacation_register_modal";

    /* =========================================
     * 3. Slack 이벤트 타입
     * ========================================= */
    public static final String VIEW_SUB_MISSION = "view_submission"; // 모달 제출
    public static final String BLOCK_ACTIONS    = "block_actions";   // 버튼/셀렉트 액션

    /* =========================================
     * 4. 사용자 안내 / 에러 메시지
     * ========================================= */
    public static final String ERROR_KEYWORD = "키워드를 입력해 주세요.";
    public static final String ERROR_NO_SUCH_NOTION_PAGE = "관련된 노션 페이지를 찾을 수 없습니다.";
    public static final String ERROR_SEND_SUMMARIZE = "⚠️요약 처리 중 오류가 발생했습니다.";
    public static final String ERROR_FAIL_DOWNLOAD_DOCUMENT = "⚠️문서 다운로드 중 오류가 발생했습니다.";
    public static final String ERROR_SEND_MESSAGE = "메시지 전송 중 오류가 발생했습니다.";
    public static final String ERROR_UNSUPPORTED_COMMAND = "지원하지 않는 커맨드입니다.";
    public static final String ERROR_UNSUPPORTED_TYPE = "지원하지 않는 타입입니다.";
    public static final String ERROR_UNKNOWN_CHANEL = "지원하지 않는 채널입니다.";
    public static final String ERROR_UPDATE_TEXT = "텍스트 업데이트 중 오류가 발생했습니다.";
    public static final String VACATION_REGISTER_GUIDE_MSG = "*휴가 등록을 진행합니다.* 버튼을 눌러 양식을 열어주세요.";

    /* =========================================
     * 5. 버튼 라벨
     * ========================================= */
    public static final String BTN_SUMMARIZE = "요약보기";
    public static final String BTN_DOCUMENT_DOWNLOAD = "파일 다운로드";
    public static final String BTN_VACATION_REGISTER = "휴가등록";

    /* =========================================
     * 6. 검색 결과 메시지 템플릿
     * ========================================= */
    public static final String SEARCH_RESULT_MEETING_TEMPLATE = "*🔍총 %d건의 회의가 검색되었습니다.*";
    public static final String SEARCH_RESULT_MEMBER_TEMPLATE = "*🔍총 %d건의 멤버가 검색되었습니다.*";
    public static final String SEARCH_RESULT_DOCUMENT_TEMPLATE = "*🔍총 %d건의 문서가 검색되었습니다.*";
    public static final String SEARCH_RESULT_VACATION_TEMPLATE = "*🔍총 %d건의 휴가자가 검색되었습니다.*";

    /* =========================================
     * 7. 파일 다운로드 링크 템플릿
     * ========================================= */
    public static final String FILE_DOWNLOAD_LINK_TEMPLATE ="📄 *%s* 파일을 다운로드하려면 아래 링크를 클릭하세요:\n%s";
}
