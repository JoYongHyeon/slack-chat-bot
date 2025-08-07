package com.chatbot.plani.planislackbot.global.util.slack;

import com.chatbot.plani.planislackbot.adapter.out.notion.dto.DocumentSearchResultDTO;
import com.chatbot.plani.planislackbot.adapter.out.notion.dto.MeetingSearchResultDTO;
import com.chatbot.plani.planislackbot.adapter.out.notion.dto.MemberSearchResultDTO;
import com.chatbot.plani.planislackbot.global.util.StringUtil;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.model.block.composition.MarkdownTextObject;
import com.slack.api.model.block.composition.PlainTextObject;
import com.slack.api.model.block.element.ButtonElement;

import java.util.List;

import static com.chatbot.plani.planislackbot.global.util.constant.slack.SlackConstant.BTN_SUMMARIZE;
import static com.chatbot.plani.planislackbot.global.util.constant.slack.SlackConstant.SUMMARIZE_ACTION_ID;

/**
 * Slack 블록 UI 생성 유틸 – 회의, 멤버별 SectionBlock 변환
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
        return SectionBlock.builder()
                .text(MarkdownTextObject.builder()
                        .text("*%s*\n%s".formatted(resultDTO.title(), resultDTO.url()))
                        .build())
                .accessory(ButtonElement.builder()
                        .text(PlainTextObject.builder().text(BTN_SUMMARIZE).build())
                        .value(resultDTO.pageId())
                        .actionId(SUMMARIZE_ACTION_ID)
                        .build())
                .build();
    }

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

    public static LayoutBlock documentSectionBlock(DocumentSearchResultDTO resultDTO) {

        return null;
    }

    /**
     * 검색 결과 개수 안내 메시지용 SectionBlock 생성.
     * - ex: "총 9건의 멤버가 검색되었습니다."
     *
     * @param message 안내 메시지(포맷 문자열, 예: "총 %d건의 멤버가 검색되었습니다.")
     * @param size    결과 개수
     * @return        Slack SectionBlock (Markdown 형식)
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
