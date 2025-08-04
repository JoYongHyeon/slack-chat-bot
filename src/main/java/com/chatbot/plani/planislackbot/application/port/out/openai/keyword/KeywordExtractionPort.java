package com.chatbot.plani.planislackbot.application.port.out.openai.keyword;


import com.chatbot.plani.planislackbot.domain.slack.vo.IntentResultVO;

/**
 * 자연어 입력 → Notion/서비스 특화 쿼리 및 인텐트 추출 포트
 *
 * - 오픈AI 등 LLM을 활용해 슬랙 등에서 받은 자연어 명령을 구조화/분류한다.
 * - 향후 다양한 명령어 추출 패턴 추가에 유연하게 대응할 수 있음.
 */
public interface KeywordExtractionPort {


    /**
     * OpenAI를 이용해 노션 DB용 쿼리(필터/정렬 조건 등) 생성
     * @param text 자연어 명령어 (예: "7월 완료된 회의 목록")
     * @return JSON 또는 텍스트 형태 쿼리 (예: {"날짜":"2025-07-01~2025-07-31","상태":"완료"})
     */
    String extractNotionQuery(String text);

    /**
     * 자연어 명령어에서 1차(서비스), 2차(세부 intent)를 추출
     * @param text 자연어 입력 (예: "노션에서 7월 회의 보여줘")
     * @return IntentResult("notion", "meeting")
     */
    IntentResultVO extractServiceIntent(String text);
}
