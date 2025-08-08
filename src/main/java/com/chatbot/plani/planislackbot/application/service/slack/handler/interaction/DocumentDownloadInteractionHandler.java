package com.chatbot.plani.planislackbot.application.service.slack.handler.interaction;

import com.chatbot.plani.planislackbot.adapter.in.web.slack.dto.SlackBlockActionDTO;
import com.chatbot.plani.planislackbot.adapter.out.notion.dto.file.DocumentFileDTO;
import com.chatbot.plani.planislackbot.application.port.in.NotionInteractionHandler;
import com.chatbot.plani.planislackbot.application.port.out.notion.file.DocumentFileDownloadPort;
import com.chatbot.plani.planislackbot.application.port.out.slack.SlackSendPort;
import com.chatbot.plani.planislackbot.global.util.slack.SlackInteractionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.chatbot.plani.planislackbot.global.util.constant.slack.SlackConstant.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentDownloadInteractionHandler implements NotionInteractionHandler {

    private final SlackSendPort slackSendPort;
    private final DocumentFileDownloadPort documentFileDownloadPort;

    @Override
    public String getActionId() {
        return DOCUMENT_DOWNLOAD_ACTION_ID;
    }

    @Override
    public void handleInteraction(SlackBlockActionDTO dto) {

        String pageId = SlackInteractionUtil.extractPageId(dto);

        String channel = SlackInteractionUtil.extractChannel(dto);

        if (pageId == null) {
            slackSendPort.sendText(channel, ERROR_FAIL_DOWNLOAD_DOCUMENT);
        }

        if (channel == null) {
            log.warn("[Slack 채널 ID] : null  메시지 내용: {}", ERROR_UNKNOWN_CHANEL);
        }

        // 1. 파일 정보 조회
        DocumentFileDTO documentFileDto = documentFileDownloadPort.getDocumentFile(pageId);

        // 2. 파일 정보가 없거나 URL 이 없으면 에러 처리
        if (documentFileDto == null || documentFileDto.fileUrl() == null) {
            slackSendPort.sendText(channel, ERROR_FAIL_DOWNLOAD_DOCUMENT);
            return;
        }

        // 3. 슬랙으로 다운로드 링크 전송
        String message = String.format(
                FILE_DOWNLOAD_LINK_TEMPLATE,
                documentFileDto.fileName(), documentFileDto.fileUrl()
        );

        slackSendPort.sendText(channel, message);
    }
}
