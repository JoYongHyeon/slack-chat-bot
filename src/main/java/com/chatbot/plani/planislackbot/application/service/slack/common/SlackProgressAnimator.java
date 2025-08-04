package com.chatbot.plani.planislackbot.application.service.slack.common;

import com.chatbot.plani.planislackbot.application.port.out.slack.SlackSendPort;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class SlackProgressAnimator {
    private final SlackSendPort slackSendPort;
    private final String channel;
    private final Executor executor;
    private volatile boolean running = true;
    private volatile Future<?> animationTask;
    private volatile String messageTs; // 메시지 타임스탬프 저장

    public SlackProgressAnimator(SlackSendPort slackSendPort,
                                 String channel,
                                 Executor executor) {
        this.slackSendPort = slackSendPort;
        this.channel = channel;
        this.executor = executor;
    }

    public void start(String baseMessage) {
        String[] progressMessages = {
                baseMessage + "... 🟩⬜⬜⬜⬜",
                baseMessage + "... 🟩🟩⬜⬜⬜",
                baseMessage + "... 🟩🟩🟩⬜⬜",
                baseMessage + "... 🟩🟩🟩🟩⬜",
                baseMessage + "... 🟩🟩🟩🟩🟩"
        };

        // 첫 메시지 전송하고 타임스탬프 받기
        slackSendPort.sendText(channel, progressMessages[0]);

        if (executor instanceof ExecutorService executorService) {
            animationTask = executorService.submit(() -> runAnimation(progressMessages));
        } else {
            executor.execute(() -> runAnimation(progressMessages));
        }
    }

    private void runAnimation(String[] progressMessages) {
        int idx = 1; // 첫 번째는 이미 전송했으므로 1부터 시작

        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                // 기존 메시지 업데이트
                slackSendPort.updateText(channel, messageTs,
                        progressMessages[idx % progressMessages.length]);
                idx++;
                Thread.sleep(1200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
            }
        }
    }

    public void stop(String finalMessage) {
        running = false;

        if (animationTask != null) {
            animationTask.cancel(true);
        }

        try {
            // 최종 메시지로 업데이트
            if (messageTs != null) {
                slackSendPort.updateText(channel, messageTs, finalMessage);
            } else {
                // fallback: 새 메시지로 전송
                slackSendPort.sendText(channel, finalMessage);
            }
        } catch (Exception e) {
        }
    }

    public void ensureStop() {
        if (running) {
            running = false;
            if (animationTask != null) {
                animationTask.cancel(true);
            }
        }
    }
}
