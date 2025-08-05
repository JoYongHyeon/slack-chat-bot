package com.chatbot.plani.planislackbot.application.port.out.openai.intent;

import com.chatbot.plani.planislackbot.domain.slack.vo.IntentResultVO;

public interface ExtractServiceIntentPort {

    /**
     * 자연어 명령에서 서비스/세부 intent 추출
     * (ex: "회의" → meeting, "멤버" → member 등)
     * @param text 사용자 입력 명령어
     * @return IntentResultVO(service, intent)
     */
    IntentResultVO extractServiceIntent(String text);
}
