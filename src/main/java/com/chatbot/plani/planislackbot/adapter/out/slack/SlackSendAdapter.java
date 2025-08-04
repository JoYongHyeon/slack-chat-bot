package com.chatbot.plani.planislackbot.adapter.out.slack;

import com.chatbot.plani.planislackbot.adapter.out.notion.NotionSearchResultDTO;
import com.chatbot.plani.planislackbot.application.port.out.slack.SlackSendPort;
import com.chatbot.plani.planislackbot.global.util.slack.SlackBlockUtil;
import com.slack.api.Slack;
import com.slack.api.model.block.DividerBlock;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.model.block.composition.MarkdownTextObject;
import com.slack.api.model.block.composition.PlainTextObject;
import jdk.jfr.Description;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.chatbot.plani.planislackbot.global.util.constant.slack.SlackConstant.*;

/**
 * 실제 Slack API 를 사용해 메시지를 전송
 */
@Component
public class SlackSendAdapter implements SlackSendPort {

    @Value("${slack.bot-token}")
    private String botToken;

    /**
     * 텍스트 메시지를 Slack 채널로 전송
     * 실패 시 예외는 무시(알림/로깅 등 필요시 추가)
     */
    @Override
    public void sendText(String channel, String message) {
        try {
            Slack.getInstance().methods(botToken)
                    .chatPostMessage(req -> req.channel(channel).text(message));
        } catch (Exception ignore) {
        }
    }

    /**
     * 노션 검색 결과 목록을 Slack 블록 메시지로 변환하여 전송
     * 실패 시 에러 메시지 별도 전송
     */
    @Override
    public void sendBlocks(String channel, List<NotionSearchResultDTO> results) {
        try {
            // Notion 검색 결과를 슬랙의 LayoutBlock 리스트로 변환
            List<LayoutBlock> blocks = new ArrayList<>();

            String fallbackText = SEARCH_RESULT_MEETING_TEMPLATE.formatted(results.size());

            // 1. 검색 결과 개수 안내 텍스트
            blocks.add(SectionBlock.builder()
                    .text(MarkdownTextObject.builder()
                            .text(fallbackText)
                            .build())
                    .build());

            // 2. Divider (구분선)
            blocks.add(DividerBlock.builder().build());

            // 3. 실제 검색 결과
            results.stream()
                    .map(SlackBlockUtil::toSectionBlock)
                    .forEach(blocks::add);

            // 실제 슬랙 API로 블록 메시지 전송
            Slack.getInstance().methods(botToken)
                    .chatPostMessage(req -> req
                            .channel(channel)
                            .blocks(blocks)
                            .text(fallbackText));

        } catch (Exception e) {
            // 블록 메시지 전송 실패 시 안내 메시지 전송
            sendText(channel, ERROR_SEND_SUMMARIZE);
        }
    }
}
