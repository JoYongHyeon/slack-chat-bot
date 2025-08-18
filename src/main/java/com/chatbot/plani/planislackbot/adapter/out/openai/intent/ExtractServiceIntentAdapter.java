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
                너는 사용자의 자연어 명령어에서 "서비스"와 "세부 intent"를 추출해서 반드시 아래 JSON 형식으로만 응답하는 역할을 한다.
                절대 설명, 예외, 사족 없이 JSON만 응답해야 한다.
                입력 문장 속 키워드만 보고 정확한 intent를 판단하라.
                주관적인 판단 없이 아래 규칙과 예시만으로 판단하라.
                반드시 아래 JSON 구조로 응답:
                {
                  "service": "<서비스명>",  // 항상 "notion" 또는 "" (빈 문자열)
                  "intent": "<세부 intent명>" // notion일 경우: meeting, document, vacation, vacation_register, member 중 하나. notion이 아니면 ""
                }
                판단 규칙 (포함 키워드 기준):
                - 사람, 연락처, 명단, 조직 등 인원 관련 키워드 → "member"
                    예: '멤버', '사람', '인원', '구성원', '팀원', '이름', '소속', '연락처', '직책', '이메일', '내선', '입사일', '팀', '부서', '전화번호부', '조직도', '명단'
        
                - 회의, 회의록, 장소, 날짜, 참석자 관련 키워드 → "meeting"
                    예: '회의', '회의록', '미팅', 'agenda', '논의', '회의실', '참석자', '진행자', '장소', '내용', '보고서', 날짜(ex: 6월, 7월, 3월 등),
                        장소명(ex: 이비자, 산토리니, 피렌체, 청운관, 발리, 아로파홀 등)
     
                - 휴가 조회/검색 관련 키워드 → "vacation"
                    예: '휴가', '연차', '오전반차', '오후반차', '반차', '병가', '기간', '휴가자', '휴무', '연차자', '휴가현황'
      
                - 휴가 등록 관련 키워드 → "vacation_register"
                    예: '휴가등록', '연차등록', '오전반차등록', '오후반차등록', '반차등록', '병가등록', '휴가 신청', '연차 신청', '반차 신청', '병가 신청'
    
                - 문서, 자료, 파일, 다운로드 등 관련 키워드 → "document"
                    예: '문서', '자료', '파일', '가이드', '업로드', '다운로드'
     
                - 위에 해당하지 않으면 "unknown"
    
                추가 조건:
                - 장소명이 나오면 무조건 "meeting"
                - 날짜(월 단위나 특정 일자 등)가 회의나 장소와 함께 나오면 → "meeting"
      
                 응답은 반드시 아래 예시처럼 **JSON만 반환**해야 한다.
       
                입력 예시:
                "멤버 목록 보여줘" → {"service":"notion","intent":"member"}
                "산토리니 회의 자료 보여줘" → {"service":"notion","intent":"meeting"}
                "3월 회의 보고서 줘" → {"service":"notion","intent":"meeting"}
                "7월 회의록" → {"service":"notion","intent":"meeting"}
                "연차 쓴 사람 목록 보여줘" → {"service":"notion","intent":"vacation"}
                "8월 연차 목록 보여줘" -> {"service":"notion","intent":"vacation"}
                "8월 12일 오전반차 쓴사람 보여줘" -> {"service":"notion","intent":"vacation"}
                "8월 12일 오후반차 쓴사람 보여줘" -> {"service":"notion","intent":"vacation"}
                "6월달 회의 보여줘" → {"service":"notion","intent":"meeting"}
                "문서 자료 모아줘" → {"service":"notion","intent":"document"}
                "회사 조직도 알려줘" → {"service":"notion","intent":"member"}
                "청운관 참석자 알려줘" → {"service":"notion","intent":"meeting"}
                "5월 회의 내용 확인" → {"service":"notion","intent":"meeting"}
                "휴가등록" → {"service":"notion","intent":"vacation_register"}
                "휴가등록 창 보여줘" → {"service":"notion","intent":"vacation_register"}
                "휴가등록 할래" → {"service":"notion","intent":"vacation_register"}
                "병가 신청" → {"service":"notion","intent":"vacation_register"}
                "8월 12일 휴가 신청 해줘" → {"service":"notion","intent":"vacation_register"}
                "8월 12일 병가 신청 해줘" → {"service":"notion","intent":"vacation_register"}
                "8월 12일 오전반차 신청 해줘" → {"service":"notion","intent":"vacation_register"}
                "8월 12일 오후반차 신청 해줘" → {"service":"notion","intent":"vacation_register"}
       
                입력: %s
        """.formatted(StringUtil.removeSlackMentionPrefix(text));

        String responseJson = openAiChatHelper.call(prompt);
        return JsonUtil.toObject(responseJson, IntentResultVO.class, objectMapper);
    }
}
