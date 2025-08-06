package com.chatbot.plani.planislackbot.global.util;

import com.chatbot.plani.planislackbot.domain.slack.vo.IntentResultVO;
import jdk.jfr.Description;

import java.util.Optional;

@Description("String 관련 유틸")
public class StringUtil {

    private StringUtil() {}

    /**
     * 문자열이 null 또는 비어있거나(공백만 포함) 하면 true를 반환합니다.
     *
     * @param s 검사할 문자열
     * @return null, "" 또는 공백만 포함되어 있으면 true, 아니면 false
     */
    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static String isEmptyHyphen(String s) {
        return s == null ? "-" : s;
    }

    /**
     * 문자열이 null도 아니고, 공백도 아니면 true를 반환합니다.
     *
     * @param s 검사할 문자열
     * @return 실제 내용이 있으면 true, 아니면 false
     */
    public static boolean isNotEmpty(String s) {
       return !isEmpty(s);
    }

    /**
     * 슬랙 멘션(@user)으로 시작하는 부분을 제거하고 순수 텍스트만 반환.
     * ex: "<@U1234> hello" → "hello"
     */
    public static String removeSlackMentionPrefix(String text) {
        return text.replaceFirst("^<@[A-Z0-9]+>\\s*", "");
    }


    /**
     * "service:intent" 형태의 문자열을 파싱하여 ServiceIntentVO로 변환합니다.
     * 예: "notion:summarize_page" -> new ServiceIntentVO("notion", "summarize_page")
     * @param actionId 파싱할 문자열
     * @return ServiceIntentVO (형식이 맞지 않으면 null)
     */
    public static Optional<IntentResultVO> parseServiceIntent(String actionId) {
        if (actionId == null || !actionId.contains(":")) return Optional.empty();
        String[] parts = actionId.split(":", 2);
        if (parts.length < 2) return Optional.empty();
        return Optional.of(new IntentResultVO(parts[0], parts[1]));
    }

}
