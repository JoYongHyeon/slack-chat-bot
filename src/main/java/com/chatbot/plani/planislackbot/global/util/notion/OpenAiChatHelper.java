package com.chatbot.plani.planislackbot.global.util.notion;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpenAiChatHelper {

    private final OpenAiChatClient openAiChatClient;

    /**
     * Open AI 에 프롬프트 전송 후 텍스트 결과 반환
     */
    public String call(String prompt) {
        ChatResponse response = openAiChatClient.call(new Prompt(new UserMessage(prompt)));
        return response.getResult().getOutput().getContent();
    }
}
