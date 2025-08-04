package com.chatbot.plani.planislackbot.global.util.notion;

import notion.api.v1.model.pages.Page;
import notion.api.v1.model.pages.PageProperty;

import java.util.Optional;

import static com.chatbot.plani.planislackbot.global.util.constant.notion.NotionConstant.ERROR_NOTION_TITLE;
import static com.chatbot.plani.planislackbot.global.util.constant.notion.NotionConstant.MEETING_TITLE;

public class NotionPageUtil {
    private NotionPageUtil() {
    }

    /**
     * Notion Page 에서 제목 추출
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
}
