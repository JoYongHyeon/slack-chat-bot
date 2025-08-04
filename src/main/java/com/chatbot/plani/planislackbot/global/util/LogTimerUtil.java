package com.chatbot.plani.planislackbot.global.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogTimerUtil {
    private LogTimerUtil() {}

    /**
     * 작업 실행 전후의 시간을 측정하고, 처리 시간과 스레드명 로그를 남김
     * @param tag 구분 태그 (예: "슬랙 이벤트")
     * @param runnable 실행할 작업 (람다)
     */
    public static void runWithTiming(String tag, Runnable runnable) {
        long start = System.currentTimeMillis();
        String threadName = Thread.currentThread().getName();
        log.info("[{}][{}] 처리 시작", tag, threadName);

        try {
            runnable.run();

        } finally {
            long end = System.currentTimeMillis();
            log.info("[{}][{}] 처리 완료 - 소요 시간: {}ms", tag, threadName, end - start);
        }
    }
}
