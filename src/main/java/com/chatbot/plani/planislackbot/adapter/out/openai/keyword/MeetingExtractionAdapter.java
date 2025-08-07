package com.chatbot.plani.planislackbot.adapter.out.openai.keyword;

import com.chatbot.plani.planislackbot.application.port.out.openai.keyword.MeetingKeywordExtractionPort;
import com.chatbot.plani.planislackbot.global.util.StringUtil;
import com.chatbot.plani.planislackbot.global.util.notion.NotionFilterUtil;
import com.chatbot.plani.planislackbot.global.util.notion.helper.OpenAiChatHelper;
import lombok.RequiredArgsConstructor;
import notion.api.v1.model.databases.query.filter.PropertyFilter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class MeetingExtractionAdapter implements MeetingKeywordExtractionPort {

    private final OpenAiChatHelper openAiChatHelper;

    @Override
    public String extractNotionQuery(String text) {

        String prompt = """
        너는 사용자의 자연어 명령을 받아, 아래 Notion DB의 실제 컬럼명과 타입에 맞는
        검색 조건(JSON)을 생성하는 쿼리 생성기야.

        ▣ Notion DB 컬럼 구조(실제 명칭과 타입):
        - 회의명 (회의명) : 텍스트
        - 날짜 (날짜) : 날짜(yyyy-MM-dd)
        - 시간 (시간) : 텍스트
        - 장소 (장소) : 선택 (예: 산토리니, 이비자)
        - 카테고리 (카테고리) : 선택
        - 상태 (상태) : 선택 (예: 완료, 진행 중, 시작 전)
        - 참석자 (참석자) : 다중선택 (사람 이름)
        - 진행자 (진행자) : 선택 (사람 이름만 값이 될 수 있음, 동사/형용사 등 불가)

        ▣ 생성 규칙(반드시 지켜야 함):
        1. 반드시 JSON 형식으로만 응답해.
        2. 날짜가 "7월"과 같이 범위로 들어오면, 날짜 범위(예: "2025-07-01~2025-07-31")로 변환해서 필터를 생성해.
        3. 값이 여러 개(OR 조건)가 들어오면, 각 조건을 배열로 반환해.
        4. "모든 회의", "전부" 등 전체 요청이면 필터를 비워서 반환.
        5. 키는 반드시 실제 컬럼명("날짜", "카테고리", "참석자" 등)으로만 쓸 것.
        6. 각 컬럼의 실제 값과 타입(예: "진행자"는 반드시 사람 이름, "상태"는 반드시 '완료/진행 중/시작 전')을 지켜라.
        7. "진행자" 컬럼에는 동사/형용사(예: 진행한, 완료한 등)가 들어가면 안 되고, 반드시 사람 이름(예: 권장환, 김은화 등)만 허용된다.
        8. "진행한", "완료한" 등은 절대 "진행자"에 들어가면 안 된다.
        
        ▣ 잘못된 예시 (절대 이렇게 반환하지 마라):
        - {"진행자": "진행한"} (잘못된 예시, "진행자"는 사람 이름만 올 수 있음)
        - {"진행자": "완료한"} (잘못된 예시)
        
        ▣ 올바른 예시:
        입력1: "이비자에서 진행한 회의 모두 보여줘"
        출력1: { "장소": "이비자" }
        입력2: "7월 완료된 회의 다 보여줘"
        출력2: { "날짜": "2025-07-01~2025-07-31", "상태": "완료" }
        입력3: "산토리니에서 진행한 주간업무현황 회의"
        출력3: { "장소": "산토리니", "카테고리": "주간업무현황" }
        입력4: "조용현, 황동훈이 참석한 회의"
        출력4: { "참석자": ["조용현", "황동훈"] }
        입력5: "모든 회의 보여줘"
        출력5: { }
        
        아래 입력에 대해 위 규칙을 반드시 지켜서 JSON 응답해.
        (반드시 JSON 만 출력해라:  {"날짜": "2025-07-21"})
        입력: %s
        """.formatted(StringUtil.removeSlackMentionPrefix(text));

        return openAiChatHelper.call(prompt);
    }

    @Override
    public List<PropertyFilter> buildFilters(Map<String, String> conditions) {
        // 조건별 Notion PropertyFilter 생성 (헬퍼 유틸 사용)
        return conditions.entrySet().stream()
                .flatMap(entry ->
                        NotionFilterUtil.buildMeetingFilters(entry.getKey(), entry.getValue()).stream())
                .toList();
    }
}

