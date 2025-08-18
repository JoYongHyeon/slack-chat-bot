package com.chatbot.plani.planislackbot.global.util.slack;

import com.chatbot.plani.planislackbot.adapter.out.notion.dto.search.DocumentSearchResultDTO;
import com.chatbot.plani.planislackbot.adapter.out.notion.dto.search.MeetingSearchResultDTO;
import com.chatbot.plani.planislackbot.adapter.out.notion.dto.search.MemberSearchResultDTO;
import com.chatbot.plani.planislackbot.adapter.out.notion.dto.search.VacationSearchResultDTO;
import com.chatbot.plani.planislackbot.global.util.StringUtil;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.model.block.composition.MarkdownTextObject;
import com.slack.api.model.block.composition.PlainTextObject;
import com.slack.api.model.block.element.ButtonElement;

import static com.chatbot.plani.planislackbot.global.util.constant.slack.SlackConstant.*;

/**
 * Slack 블록 UI 생성 유틸리티
 * - Notion 검색 결과를 Slack 메시지 Block 형태로 변환
 * - 각 DB 유형(회의, 멤버, 문서, 휴가)에 맞는 Block 생성 메서드 제공
 */

public class SlackBlockUtil {
    public SlackBlockUtil() {
    }

    /**
     * [회의용] Notion 검색 결과를 Slack SectionBlock(요약 버튼 포함)으로 변환
     * <p>
     * - 회의 DB 전용(제목/URL/요약 버튼)
     * - 멤버 등 다른 용도는 별도 메서드 작성 권장
     *
     * @param resultDTO Notion 회의 검색 결과 DTO
     * @return 슬랙 SectionBlock (제목, URL, 요약 버튼 포함)
     */
    public static LayoutBlock meetingSectionBlock(MeetingSearchResultDTO resultDTO) {

        String meetingTitle = "*%s*\n%s".formatted(resultDTO.title(), resultDTO.url());

        ButtonElement summarizeButton = ButtonElement.builder()
                .text(PlainTextObject.builder().text(BTN_SUMMARIZE).build())
                .url(resultDTO.url())
                .actionId(SUMMARIZE_ACTION_ID)
                .value(resultDTO.pageId())
                .build();

        return SectionBlock.builder()
                .text(MarkdownTextObject.builder().text(meetingTitle).build())
                .accessory(summarizeButton)
                .build();
    }

    /**
     * 멤버 검색 결과 → Slack SectionBlock 변환
     * - 이름, 소속, 직책, 연락처 등 표시
     */
    public static LayoutBlock memberSectionBlock(MemberSearchResultDTO resultDTO) {

        String member = """
                👤 *%s*
                • 소속: %s
                • 직책: %s
                • 연락처: %s
                • 이메일: %s
                • 내선: %s
                • 입사일: %s
                """.formatted(
                StringUtil.isEmptyHyphen(resultDTO.name()),
                StringUtil.isEmptyHyphen(resultDTO.team()),
                StringUtil.isEmptyHyphen(resultDTO.role()),
                StringUtil.isEmptyHyphen(resultDTO.contact()),
                StringUtil.isEmptyHyphen(resultDTO.email()),
                StringUtil.isEmptyHyphen(resultDTO.extension()),
                StringUtil.isEmptyHyphen(resultDTO.joinDate())
        );

        return SectionBlock.builder()
                .text(MarkdownTextObject.builder().text(member).build())
                .build();
    }

    /**
     * 문서 검색 결과 → Slack SectionBlock 변환
     * - 파일명 + URL + "다운로드" 버튼
     */
    public static LayoutBlock documentSectionBlock(DocumentSearchResultDTO resultDTO) {

        // 파일 명, 파일 링크, 다운로드 버튼
        String fileTitle = "*%s*\n%s".formatted(resultDTO.fileName(), resultDTO.url());

        // 버튼의 Slack 의 ButtonElement 로 URL 이동 (다운로드X, 링크 이동)
        ButtonElement downloadButton = ButtonElement.builder()
                .text(PlainTextObject.builder().text(BTN_DOCUMENT_DOWNLOAD).build())
                .url(resultDTO.url())
                .actionId(DOCUMENT_DOWNLOAD_ACTION_ID)
                .value(resultDTO.pageId())
                .build();

        return SectionBlock.builder()
                .text(MarkdownTextObject.builder().text(fileTitle).build())
                .accessory(downloadButton)
                .build();
    }

    /**
     * 휴가 검색 결과 → Slack SectionBlock 변환
     * - 신청자, 유형, 기간, 상태, 사유 표시
     */
    public static LayoutBlock vacationSectionBlock(VacationSearchResultDTO resultDTO) {

        String vacation = """
                👤 *%s*
                • 유형: %s
                • 시작일: %s
                • 종료일: %s
                • 상태: %s
                • 사유: %s
                """.formatted(
                StringUtil.isEmptyHyphen(resultDTO.applicantName()),
                StringUtil.isEmptyHyphen(resultDTO.vacationType()),
                StringUtil.isEmptyHyphen(resultDTO.startDate()),
                StringUtil.isEmptyHyphen(resultDTO.endDate()),
                StringUtil.isEmptyHyphen(resultDTO.status()),
                StringUtil.isEmptyHyphen(resultDTO.reason()));

        return SectionBlock.builder()
                .text(MarkdownTextObject.builder().text(vacation).build())
                .build();
    }

    /**
     * "휴가 등록" 모달 열기 버튼 Block
     */
    public static LayoutBlock vacationRegisterOpenModalBlock() {

        ButtonElement openModalButton = ButtonElement.builder()
                .text(PlainTextObject.builder().text(BTN_VACATION_REGISTER).build())
                .actionId(VACATION_REGISTER_ACTION_ID)
                .build();

        return SectionBlock.builder()
                .text(MarkdownTextObject.builder()
                        .text(VACATION_REGISTER_GUIDE_MSG).build())
                .accessory(openModalButton)
                .build();
    }

    /**
     * 검색 결과 개수 안내 메시지용 SectionBlock 생성.
     * - ex: "총 9건의 멤버가 검색되었습니다."
     *
     * @param message 안내 메시지(포맷 문자열, 예: "총 %d건의 멤버가 검색되었습니다.")
     * @param size    결과 개수
     * @return Slack SectionBlock (Markdown 형식)
     */
    public static SectionBlock resultCountBlock(String message, int size) {
        String searchResultCountMsg = message.formatted(size);
        return SectionBlock.builder()
                .text(MarkdownTextObject.builder()
                        .text(searchResultCountMsg)
                        .build())
                .build();
    }
}
