package com.chatbot.plani.planislackbot.global.util.notion;

import com.chatbot.plani.planislackbot.adapter.out.notion.dto.file.DocumentFileDTO;
import notion.api.v1.model.pages.Page;
import notion.api.v1.model.pages.PageProperty;

import java.util.List;
import java.util.Optional;

/**
 * Notion 파일 프로퍼티에서 파일명/URL 안전 추출 유틸
 */
public class NotionFileUtil {

    private NotionFileUtil() {
    }

    /**
     * PageProperty에서 첫 번째 파일의 이름/URL 추출
     * (파일이 없으면 null 반환)
     */
    public static DocumentFileDTO extractFileInfo(PageProperty fileProperty) {
        return Optional.ofNullable(fileProperty)
                .map(PageProperty::getFiles)
                .filter(files -> !files.isEmpty())
                .map(List::getFirst)
                .map(f -> new DocumentFileDTO(
                        f.getName(),
                        f.getFile() != null ? f.getFile().getUrl() : null
                ))
                .orElse(null);
    }

    /**
     * Page + propertyName으로 파일 정보 추출 (내부적으로 위 메서드 호출)
     */
    public static DocumentFileDTO extractFileInfo(Page page, String propertyName) {
        if (page == null || propertyName == null) return null;
        PageProperty fileProperty = page.getProperties().get(propertyName);
        return extractFileInfo(fileProperty);
    }
}
