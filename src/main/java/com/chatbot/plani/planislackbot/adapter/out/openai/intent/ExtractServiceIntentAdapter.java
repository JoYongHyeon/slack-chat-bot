package com.chatbot.plani.planislackbot.adapter.out.openai.intent;

import com.chatbot.plani.planislackbot.application.port.out.openai.intent.ExtractServiceIntentPort;
import com.chatbot.plani.planislackbot.domain.slack.vo.IntentResultVO;
import com.chatbot.plani.planislackbot.global.util.JsonUtil;
import com.chatbot.plani.planislackbot.global.util.StringUtil;
import com.chatbot.plani.planislackbot.global.util.notion.helper.OpenAiChatHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * OpenAI 기반 키워드(의도) 추출 어댑터
 */
@Component
@RequiredArgsConstructor
public class ExtractServiceIntentAdapter implements ExtractServiceIntentPort {

    private final OpenAiChatHelper openAiChatHelper;
    private final ObjectMapper objectMapper;

    @Override
    public IntentResultVO extractServiceIntent(String text) {
        String prompt = """
        너는 사용자의 자연어 명령어에서 서비스와 세부 intent(특히 notion)을 추출해서 아래 JSON 반환하는 역할을 한다.

        반드시 아래 JSON 형태만 응답한다(설명, 예외 없이):
        {
          "service": "<서비스명>",  // notion, 중 하나
          "intent": "<세부 intent명>" // notion일 때만: meeting, document, vacation, member 중 하나. notion이 아니면 ""
        }

        규칙:
        - '멤버', '사람', '인원', '구성원', '팀원', '이름', '소속', '연락처', '직책', '이메일', '내선', '입사일', '팀', '부서', '전화번호부', '조직도', '명단' 등 인원 관련 표현이 포함되면 "member"
        - '회의', '회의록', '미팅', 'agenda', '논의', '회의실', '참석자', '진행자', '장소', 장소명(이비자, 산토리니, 피렌체, 칸쿤, 청운관, 발리, 하와이, 아로파홀, 몰디브 등)이 포함되면 "meeting"
        - '휴가', '연차', '휴가자', '휴가현황', '휴무', '연차자' 등은 "vacation"
        - '문서', '파일', '자료', '다운로드', '업로드', '가이드' 등은 "document"
        - 위에 해당하지 않으면 "unknown"
        - 장소명이 나오면 무조건 meeting

        - 반드시 아래 예시처럼 JSON만 반환
        입력 예시1: 멤버 목록 보여줘
        출력 예시1: {"service":"notion","intent":"member"}
        
        입력 예시2: 신동호 연락처 알려줘
        출력 예시2: {"service":"notion","intent":"member"}
        
        입력 예시3: DevOps1에 누가 있어?
        출력 예시3: {"service":"notion","intent":"member"}
        
        입력 예시4: 연차 쓴 사람 목록 보여줘
        출력 예시4: {"service":"notion","intent":"vacation"}
        
        입력 예시5: 7월 회의록 보여줘
        출력 예시5: {"service":"notion","intent":"meeting"}
        
        입력 예시6: 회사 조직도 알려줘
        출력 예시6: {"service":"notion","intent":"member"}
        
        입력 예시7: 문서 자료 모아줘
        출력 예시7: {"service":"notion","intent":"document"}
        
        입력 예시8: 회사 멤버십 명단
        출력 예시8: {"service":"notion","intent":"member"}
        
        입력 예시9: 사내 전화번호부 보여줘
        출력 예시9: {"service":"notion","intent":"member"}
        
        입력 예시10: 산토리니에서 했던 회의 보여줘
        출력 예시10: {"service":"notion","intent":"meeting"}
        
        입력 예시11: 7월 28일 이비자 회의록
        출력 예시11: {"service":"notion","intent":"meeting"}
        
        입력 예시12: 청운관 참석자 알려줘
        출력 예시12: {"service":"notion","intent":"meeting"}
        
        입력 예시13: 아로파홀 회의 내용 보여줘
        출력 예시13: {"service":"notion","intent":"meeting"}

        입력: %s
        """.formatted(StringUtil.removeSlackMentionPrefix(text));

        String responseJson = openAiChatHelper.call(prompt);
        return JsonUtil.toObject(responseJson, IntentResultVO.class, objectMapper);
    }
}
