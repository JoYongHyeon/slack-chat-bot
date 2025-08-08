package com.chatbot.plani.planislackbot.adapter.out.notion.file;

import com.chatbot.plani.planislackbot.adapter.out.notion.dto.file.DocumentFileDTO;
import com.chatbot.plani.planislackbot.application.port.out.notion.file.DocumentFileDownloadPort;
import com.chatbot.plani.planislackbot.global.util.notion.NotionFileUtil;
import lombok.RequiredArgsConstructor;
import notion.api.v1.NotionClient;
import notion.api.v1.model.pages.Page;
import notion.api.v1.request.pages.RetrievePageRequest;
import org.springframework.stereotype.Component;


import static com.chatbot.plani.planislackbot.global.util.constant.notion.NotionConstant.DOCUMENT_FILE;

@Component
@RequiredArgsConstructor
public class DocumentFileDownloadAdapter implements DocumentFileDownloadPort {

    private final NotionClient notionClient;

    @Override
    public DocumentFileDTO getDocumentFile(String pageId) {

        // 1. 노션 페이지 조회
        RetrievePageRequest request = new RetrievePageRequest(pageId);
        Page page = notionClient.retrievePage(request);

        // 2. 파일 정보 추출 (예: "파일" 속성)
        return NotionFileUtil.extractFileInfo(page, DOCUMENT_FILE);
    }
}
