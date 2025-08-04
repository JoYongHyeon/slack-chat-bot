package com.chatbot.plani.planislackbot.global.util;

import jdk.jfr.Description;

@Description("String 관련 유틸")
public class StringUtil {

    /**
     * 문자열이 null 또는 비어있거나(공백만 포함) 하면 true를 반환합니다.
     *
     * @param s 검사할 문자열
     * @return null, "" 또는 공백만 포함되어 있으면 true, 아니면 false
     */
    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
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
}
