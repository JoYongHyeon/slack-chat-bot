package com.chatbot.plani.planislackbot.global.util.notion;

import notion.api.v1.model.blocks.*;
import notion.api.v1.model.pages.PageProperty;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class NotionBlockUtil {

    private NotionBlockUtil() {}

    private static String toPlainText(List<PageProperty.RichText> richTexts) {
        return richTexts.stream()
                .map(PageProperty.RichText::getPlainText)
                .collect(Collectors.joining("\n"));
    }

    /**
     * 개별 Block 에서 텍스트만 추출 (지원하는 타입만)
     */
    public static Optional<String> extractAllPlainText(Block block) {
        if (block instanceof ParagraphBlock paragraph) {
            return Optional.of(toPlainText(paragraph.getParagraph().getRichText()));
        }

        if (block instanceof HeadingOneBlock headingOne) {
            return Optional.of(toPlainText(headingOne.getHeading1().getRichText()));
        }

        if (block instanceof HeadingTwoBlock headingTwo) {
            return Optional.of(toPlainText(headingTwo.getHeading2().getRichText()));
        }

        if (block instanceof HeadingThreeBlock headingThree) {
            return Optional.of(toPlainText(headingThree.getHeading3().getRichText()));
        }

        if (block instanceof BulletedListItemBlock bulleted) {
            return Optional.of(toPlainText(bulleted.getBulletedListItem().getRichText()));
        }

        if (block instanceof NumberedListItemBlock numbered) {
            return Optional.of(toPlainText(numbered.getNumberedListItem().getRichText()));
        }

        return Optional.empty();
    }
}
