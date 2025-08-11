package com.chatbot.plani.planislackbot.adapter.out.openai.keyword;


import com.chatbot.plani.planislackbot.application.port.out.openai.keyword.VacationKeywordExtractionPort;
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
public class VacationExtractionAdapter implements VacationKeywordExtractionPort {

    private final OpenAiChatHelper openAiChatHelper;

    @Override
    public String extractNotionQuery(String text) {
        String prompt = """
                너는 사용자의 자연어 명령을 받아, 아래 Notion '휴가 관리 DB' 의 실제 컬럼명과 타입에 맞는
                검색 조건(JSON)을 생성하는 쿼리 생성기다.
                
                ▣ Notion 휴가 DB 컬럼 구조(실제 명칭과 타입):
                - 신청자 (신청자) : 텍스트 (예: 손민구, 설동비)
                - 유형 (유형) : 선택 (예: 연차, 오전반차, 오후반차, 기타, 병가)
                - 시작일 (시작일) : 날짜 (yyyy-MM-dd)
                - 종료일 (종료일) : 날짜 (yyyy-MM-dd)
                - 상태 (상태) : 선택 (예: 신청, 승인, 거절)
                - 담당자 (담당자) : 선택 (예: 권장환, 조용현, …)
                - 사유 (사유) : 일반 텍스트 (예: 개인사정, 병가 등)
                
                ▣ 생성 규칙 (반드시 지켜야 함):
                1. 반드시 JSON 형식으로만 응답. 다른 텍스트나 설명 금지.
                2. 키는 반드시 실제 컬럼명("신청자", "유형", "시작일", "종료일", "상태", "담당자", "사유")만 사용.
                3. 값은 해당 컬럼의 실제 타입과 존재하는 값만 허용.
                   - 유형: 연차, 오전반차, 오후반차, 병가, 기타
                   - 상태: 신청, 승인, 거절
                   - 담당자: DB에 존재하는 선택 값만
                   - 날짜: yyyy-MM-dd 형식. "8월 14일" → "2025-08-14"로 변환.
                4. 특정 월/주/일 범위 언급 시:
                   - "2025년 8월 휴가" → 시작일=2025-08-01, 종료일=2025-08-31
                   - "이번 주" → 해당 주의 월요일~일요일
                5. "모든 휴가", "전체", "전부" 등 전체 요청이면 필터를 비워서 반환: { }
                6. 개인정보(신청자, 사유)는 명확히 요청한 경우에만 포함.
                7. 불명확하거나 DB에 없는 값은 절대 포함하지 않는다.
                
                ▣ 잘못된 예시 (절대 금지):
                - {"유형": "풀타임"} (DB에 없는 값)
                - {"상태": "대기"} (DB에 없는 값)
                - {"시작일": "08/14"} (형식 틀림)
                
                ▣ 올바른 예시:
                입력1: "연차 신청 목록 보여줘"
                출력1: { }
                
                입력2: "8월 14일 오전반차 사용한 사람"
                출력2: { "유형": "오전반차", "시작일": "2025-08-14", "종료일": "2025-08-14" }
                
                입력3: "상태가 승인인 휴가 목록"
                출력3: { "상태": "승인" }
                
                입력4: "8월 휴가자 명단"
                출력4: { "시작일": "2025-08-01", "종료일": "2025-08-31" }
                
                입력5: "담당자가 권장환인 신청서"
                출력5: { "담당자": "권장환"}
                
                입력6: "신청자 손민구가 사용한 연차"
                출력6: { "신청자": "손민구", "유형": "연차" }
                
                입력8: "전체 휴가 현황"
                출력8: { }
                
                입력9: "거절된 휴가 중 사유가 개인사정인 건"
                출력9: { "상태": "거절", "사유": "개인사정" }
                
                입력10: "거절된 휴가 보여줘"
                출력10: { "상태": "거절" }
                
                아래 입력에 대해 위 규칙을 반드시 지켜서 JSON 응답만 생성해.
                입력: %s
                """.formatted(StringUtil.removeSlackMentionPrefix(text));

        return openAiChatHelper.call(prompt);
    }

    @Override
    public List<PropertyFilter> buildFilters(Map<String, String> conditions) {
        // 조건별 Notion PropertyFilter 생성 (헬퍼 유틸 사용)
        return conditions.entrySet().stream()
                .flatMap(entry ->
                        NotionFilterUtil.buildVacationFilters(entry.getKey(), entry.getValue()).stream())
                .toList();
    }
}
