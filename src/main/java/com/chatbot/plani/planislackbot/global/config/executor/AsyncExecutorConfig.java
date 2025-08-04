package com.chatbot.plani.planislackbot.global.config.executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncExecutorConfig {

    @Bean("slackToNotionExecutor")
    /**
     * 병렬 작업 처리를 위한 스레드풀 Executor Bean 등록
     *
     * - corePoolSize: 기본적으로 유지할 스레드 개수
     * - maxPoolSize: 최대 허용되는 스레드 개수
     * - queueCapacity: 대기 큐에 저장 가능한 작업 수
     * - threadNamePrefix: 생성되는 스레드 이름 접두사
     *
     * @return Executor (빈 이름: "slackToNotionExecutor")
     */
    public Executor ncpExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(8);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("Slack-Executor-");
        executor.initialize();  // Executor 초기화
        return executor;
    }
}
