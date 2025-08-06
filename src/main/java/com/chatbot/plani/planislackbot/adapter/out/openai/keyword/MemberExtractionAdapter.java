package com.chatbot.plani.planislackbot.adapter.out.openai.keyword;

import com.chatbot.plani.planislackbot.application.port.out.openai.keyword.MemberKeywordExtractionPort;
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
public class MemberExtractionAdapter implements MemberKeywordExtractionPort {

    private final OpenAiChatHelper openAiChatHelper;

    @Override
    public String extractNotionQuery(String text) {

        String prompt = """
                너는 사용자의 자연어 명령을 받아, 아래 Notion '멤버 DB' 의 실제 컬럼명과 타입에 맞는
                검색 조건(JSON)을 생성하는 쿼리 생성기야.
                
                ▣ Notion 멤버 DB 컬럼 구조(실제 명칭과 타입):
                - 이름 (이름) : 텍스트 (예: 권장환, 김은화)
                - 소속 (소속) : 선택 (예: ALL, BizOps, CloudOps, DevOps1, DevOps2)
                - 직책 (직책) : 선택 (예: 관리자, PO, SE, PRO, FD)
                - 이메일 (이메일) : 이메일 (예: test@plani.co.kr)
                - 연락처 (연락처) : 전화번호 (예: 010-1234-5678)
                - 내선 번호 (내선 번호) : 전화번호 (예: 9683)
                - 입사일 (입사일) : 날짜(yyyy-MM-dd)
                
                ▣ 생성 규칙(반드시 지켜야 함):
                1. 반드시 JSON 형식으로만 응답해.
                2. "2025년 2월에 입사한 사람"처럼 기간 조건이 들어오면, 날짜 범위(예: "2025-02-01~2025-02-28")로 변환해서 필터를 생성해.
                3. 값이 여러 개(OR 조건)가 들어오면, 각 조건을 배열로 반환해.
                4. "모든 멤버", "전체", "전부" 등 전체 요청이면 필터를 비워서 반환.
                5. 키는 반드시 실제 컬럼명("이름", "소속", "직책", "입사일" 등)으로만 쓸 것.
                6. 각 컬럼의 실제 값과 타입(예:
                 "소속"은 ALL, BizOps, ...만,
                 "직책"은 PRO, PO 등,
                 "이메일"은 이메일 형식,
                 "연락처"/"내선 번호"는 전화번호 형식,
                 "입사일"은 날짜)만 허용된다.
                7. "연락처", "이메일", "내선 번호" 등 개인정보는 명확하게 요청한 경우에만 포함한다.
                
                ▣ 잘못된 예시 (절대 이렇게 반환하지 마라):
                - {"직책": "엔지니어"} (잘못된 예시, "직책"은 SE, PO, PRO, FD, 관리자 등 실존하는 값만)
                - {"소속": "개발팀"} (잘못된 예시, "소속"은 ALL, BizOps, CloudOps, DevOps1, DevOps2 만)
                
                ▣ 올바른 예시:
                입력1: "DevOps1 소속의 모든 PRO 멤버 보여줘"
                출력1: { "소속": "DevOps1", "직책": "PRO" }
                
                입력2: "2025년 2월 입사한 직원 명단"
                출력2: { "입사일": "2025-02-01~2025-02-28" }
                
                입력3: "PO 직책 멤버 목록"
                출력3: { "직책": "PO" }
                
                입력4: "ALL 소속 모두 보여줘"
                출력4: { "소속": "ALL" }
                
                입력5: "전체 멤버 연락처 알려줘"
                출력5: { }
                
                입력6: "CloudOps 소속 PO, SE 담당자 이메일 보여줘"
                출력6: { "소속": "CloudOps", "직책": ["PO", "SE"] }
                
                입력7: "내선 번호가 9683인 멤버 찾아줘"
                출력7: { "내선 번호": "9683" }
                
                아래 입력에 대해 위 규칙을 반드시 지켜서 JSON 응답해.
                (반드시 JSON 만 출력해라: {"직책": "PRO"})
                입력: %s
                """.formatted(StringUtil.removeSlackMentionPrefix(text));

        return openAiChatHelper.call(prompt);
    }

    @Override
    public List<PropertyFilter> buildFilters(Map<String, String> conditions) {
        // 조건별 Notion PropertyFilter 생성 (헬퍼 유틸 사용)
        return conditions.entrySet().stream()
                .flatMap(entry ->
                        NotionFilterUtil.buildMemberFilters(entry.getKey(), entry.getValue()).stream())
                .toList();
    }
}
