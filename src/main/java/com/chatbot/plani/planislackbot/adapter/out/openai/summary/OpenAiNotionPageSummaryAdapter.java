package com.chatbot.plani.planislackbot.adapter.out.openai.summary;

import com.chatbot.plani.planislackbot.application.port.out.openai.summary.OpenAiNotionPageSummaryPort;
import com.chatbot.plani.planislackbot.global.util.notion.NotionBlockUtil;
import com.chatbot.plani.planislackbot.global.util.notion.helper.OpenAiChatHelper;
import lombok.RequiredArgsConstructor;
import notion.api.v1.NotionClient;
import notion.api.v1.model.blocks.Block;
import notion.api.v1.request.blocks.RetrieveBlockChildrenRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OpenAiNotionPageSummaryAdapter implements OpenAiNotionPageSummaryPort {

    private final NotionClient notionClient;
    private final OpenAiChatHelper  openAiChatHelper;

    @Override
    public String summarizePage(String pageId) {

        RetrieveBlockChildrenRequest request = new RetrieveBlockChildrenRequest(pageId);

        // 1. 블록(본문) 전체 조회
        List<Block> blocks = notionClient.retrieveBlockChildren(request).getResults();

        // 2. 노션 본문 텍스트
        String fullText = blocks.stream()
                .map(NotionBlockUtil::extractAllPlainText)
                .flatMap(Optional::stream)
                .collect(Collectors.joining());

        // 3. 노션 본문 프롬프트 추가
        String prompt = """
                다음은 회사 내부에서 작성된 노션 페이지의 전체 본문입니다.
                
                ▣ 너의 임무:
                - 페이지 전체 내용을 빠짐없이 읽고, *분류/항목별로 핵심 내용을 한국어로 요약*해줘.
                - 너무 장황하게 늘이지 말고, 핵심/주요 정보 중심으로 명확하게 정리.
                - 각 분류(또는 소제목)가 있다면 분류별로 나눠서 정리해줘.
                - 분류가 없거나 전체가 하나의 글이면, 자연스럽게 주요 내용만 정리.
                - 불필요한 반복, 장식적 문구, 덜 중요한 정보는 생략 가능.
                - 내용이 너무 짧으면 요약하지 말고 원문을 그대로 보여줘.
                
                ▣ 출력 형식:
                - 각 분류별 소제목 + 한두 줄 요약
                - 항목이 많은 경우 bullet point 사용 (ex: -, • )
                - 가능한 한 내용을 보기 쉽게 Markdown 형식(소제목, bullet 등)으로 구성
                
                아래가 전체 노션 본문이다.
                ---
                %s
                ---
                """.formatted(fullText);

        return openAiChatHelper.call(prompt);
    }
}
