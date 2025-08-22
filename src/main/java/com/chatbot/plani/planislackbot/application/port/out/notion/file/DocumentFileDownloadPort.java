package com.chatbot.plani.planislackbot.application.port.out.notion.file;

import com.chatbot.plani.planislackbot.adapter.out.notion.dto.file.DocumentFileDTO;


public interface DocumentFileDownloadPort {

    // 파일 다운로드에 필요한 정보를 반환하는 Port
    DocumentFileDTO getDocumentFile(String pageId);
}
