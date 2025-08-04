package com.chatbot.plani.planislackbot.global.util.slack;

import com.chatbot.plani.planislackbot.adapter.out.notion.NotionSearchResultDTO;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.model.block.composition.MarkdownTextObject;
import com.slack.api.model.block.composition.PlainTextObject;
import com.slack.api.model.block.element.ButtonElement;

import static com.chatbot.plani.planislackbot.global.util.constant.slack.SlackConstant.BTN_SUMMARIZE;
import static com.chatbot.plani.planislackbot.global.util.constant.slack.SlackConstant.SUMMARIZE_ACTION_ID;

/**
 * Block, Button 등 슬랙 메시지 템플릿 생성
 */
public class SlackBlockUtil {
    public SlackBlockUtil() {}

    /**
     * Notion 검색 결과를 Slack SectionBlock(버튼 포함)으로 변환
     *
     * @param resultDTO Notion 검색 결과 DTO
     * @return 슬랙 SectionBlock (제목, URL, 요약 버튼 포함)
     */
    public static LayoutBlock toSectionBlock(NotionSearchResultDTO resultDTO) {
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
}
