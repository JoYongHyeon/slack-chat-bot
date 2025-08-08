package com.chatbot.plani.planislackbot.global.util.constant.slack;

public class SlackConstant {

    // Slack JSON payload 내에서 사용하는 필드명 상수 모음
    public static final String ACTION = "actions";
    public static final String ACTION_ID = "action_id";
    public static final String VALUE = "value";
    public static final String CHANNEL = "channel";
    public static final String CHANNEL_ID = "channel_id";
    public static final String ID = "id";

    /**
     * Slack Json payload 내에서     @Override
     *     public String getActionId() {
     *         return SUMMARIZE_ACTION_ID;
     *     }사용하는 action_id 값 지정
     * ex) "요약하기" 버튼 action_id -> summarize_page
     */
    public static final String SUMMARIZE_ACTION_ID = "notion:summarize_page";
    public static final String DOCUMENT_DOWNLOAD_ACTION_ID = "notion:document_download";


    // 사용자 안내/에러 등 모든 슬랙 메시지 상수 관리
    public static final String ERROR_KEYWORD = "키워드를 입력해 주세요.";
    public static final String ERROR_NO_SUCH_NOTION_PAGE = "관련된 노션 페이지를 찾을 수 없습니다.";
    public static final String ERROR_SEND_SUMMARIZE = "⚠️요약 처리 중 오류가 발생했습니다.";
    public static final String ERROR_FAIL_DOWNLOAD_DOCUMENT = "⚠️문서 다운로드 중 오류가 발생했습니다.";
    public static final String ERROR_SEND_MESSAGE = "메시지 전송 중 오류가 발생했습니다.";
    public static final String ERROR_UNSUPPORTED_COMMAND = "지원하지 않는 커맨드입니다.";
    public static final String ERROR_UNSUPPORTED_TYPE = "지원하지 않는 타입입니다.";
    public static final String ERROR_UNKNOWN_CHANEL = "지원하지 않는 채널입니다.";
    public static final String ERROR_UPDATE_TEXT = "텍스트 업데이트 중 오류가 발생했습니다.";
    // Block 및 버튼
    public static final String BTN_SUMMARIZE = "요약보기";
    public static final String BTN_DOCUMENT_DOWNLOAD = "파일 다운로드";

    // 검색 결과 안내 메시지(포맷)🔍
    public static final String SEARCH_RESULT_MEETING_TEMPLATE = "*🔍총 %d건의 회의가 검색되었습니다.*";
    public static final String SEARCH_RESULT_MEMBER_TEMPLATE = "*🔍총 %d건의 멤버가 검색되었습니다.*";
    public static final String SEARCH_RESULT_DOCUMENT_TEMPLATE = "*🔍총 %d건의 문서가 검색되었습니다.*";

    public static final String FILE_DOWNLOAD_LINK_TEMPLATE ="📄 *%s* 파일을 다운로드하려면 아래 링크를 클릭하세요:\n%s";
}
