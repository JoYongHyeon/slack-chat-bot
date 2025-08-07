package com.chatbot.plani.planislackbot.adapter.out.openai.keyword;

import com.chatbot.plani.planislackbot.application.port.out.openai.keyword.DocumentKeywordExtractionPort;
import com.chatbot.plani.planislackbot.global.util.StringUtil;
import com.chatbot.plani.planislackbot.global.util.notion.helper.OpenAiChatHelper;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DocumentExtractionAdapter implements DocumentKeywordExtractionPort {

    private final OpenAiChatHelper openAiChatHelper;

    @Override
    public String extractNotionQuery(String text) {
        String prompt = """
                너는 사용자의 자연어 명령을 받아, 아래 Notion '문서 DB' 의 실제 컬럼명과 타입에 맞는
                검색 조건(JSON)을 생성하는 쿼리 생성기야.
                
                ▣ Notion 문서 DB 컬럼 구조(실제 명칭과 타입):
                - 파일명 (파일명) : 텍스트 (예: 보고서_최종, 사내가이드, 지출결의서 샘플)
                - 프로젝트 (프로젝트) : 선택 (예: 스케일팀스, CMP, 프로젝트C 등)
                - 파일 (파일) : 파일 & 미디어 (값 없음, 필터 불가)
                - 카테고리 (카테고리) : 선택 (예: 산출물, 매뉴얼, 증빙자료 등)
                - 업로드일 (업로드일) : 날짜 (yyyy-MM-dd)
                - 업로더 (업로더) : 선택 (예: 조용현, 박명희, 설동비 등)
                - 상태 (상태) : 선택 (예: 최신, 보관, 폐기 등)
                - 설명 (설명) : 텍스트 (자유 입력)
                
                ▣ 생성 규칙(반드시 지켜야 함):
                1. 반드시 JSON 형식으로만 응답해.
                2. 날짜가 "7월"처럼 들어오면 날짜 범위(예: "2025-07-01~2025-07-31")로 변환해서 "업로드일" 필터를 생성해.
                3. "선택" 속성은 단일 값만 허용되므로, 여러 값이 들어와도 배열로 반환하지 말고, 하나씩 개별 조건으로 처리하거나 호출처에서 OR 조건으로 분리해.
                4. "모든 문서", "전체", "전부" 등 전체 요청이면 필터를 비워서 반환해.
                5. 키는 반드시 실제 컬럼명("프로젝트", "카테고리", "업로더", "업로드일", "상태" 등)으로만 쓸 것.
                6. 존재하지 않는 값은 절대 사용하지 말고, 선택 가능한 값(예: 카테고리는 산출물/매뉴얼 등)만 사용해.
                7. 설명, 파일명은 자유 텍스트이므로 반드시 명확한 요청이 있을 때만 조건에 포함해.
                8. "최신 문서", "보관된 문서", "폐기된 문서" 등 상태 조건은 반드시 실제 상태 값으로만 대응해.
                9. 업로더/프로젝트는 사람 또는 프로젝트 이름이어야 하며, 동사/형용사(예: 업로드한 사람, 프로젝트 관련)는 절대 사용하지 마.
                
                ▣ 잘못된 예시 (절대 이렇게 반환하지 마라):
                - {"카테고리": "결과물"} (잘못된 예시, "카테고리"는 '산출물', '매뉴얼', '증빙자료' 중 하나만 가능)
                - {"업로더": "업로드한 사람"} (잘못된 예시, "업로더"는 사람 이름만 가능)
                - {"프로젝트": "관련된 프로젝트"} (잘못된 예시, "프로젝트"는 선택지 값만 가능)
                
                ▣ 올바른 예시:
                입력1: "CMP 프로젝트 문서 다 보여줘"
                출력1: { "프로젝트": "CMP" }
                
                입력2: "7월에 업로드된 산출물 문서"
                출력2: { "업로드일": "2025-07-01~2025-07-31", "카테고리": "산출물" }
                
                입력3: "조용현이 올린 최신 문서"
                출력3: { "업로더": "조용현", "상태": "최신" }
                
                입력4: "전체 문서 보여줘"
                출력4: { }
                
                입력5: "폐기된 문서만 찾아줘"
                출력5: { "상태": "폐기" }
                
                입력6: "7월 19일에 조용현이 올린 문서"
                출력6: { "업로드일": "2025-07-19", "업로더": "조용현" }
                
                아래 입력에 대해 위 규칙을 반드시 지켜서 JSON 응답해.
                (반드시 JSON만 출력할 것: 예: {"카테고리": "산출물"})
                입력: %s
                """.formatted(StringUtil.removeSlackMentionPrefix(text));

        return openAiChatHelper.call(prompt);
    }

    @Override
    public List<PropertyFilter> buildFilters(Map<String, String> conditions) {
        return List.of();
    }

}
