package com.chatbot.plani.planislackbot.application.port.out.slack;

import com.slack.api.model.view.View;

/**
 * Slack 모달(View) 관련 Outbound Port.
 * - 도메인/애플리케이션 레이어는 이 Port만 의존한다.
 * - 실제 Slack API 호출은 Adapter가 담당.
 */
public interface SlackModalPort {

    /**
     * Slack 모달 열기
     * @param triggerId 버튼/액션으로부터 전달되는 3초 유효 트리거 ID
     * @param view      열고자 하는 모달 View 정의
     */
    void openModal(String triggerId, View view);
}
