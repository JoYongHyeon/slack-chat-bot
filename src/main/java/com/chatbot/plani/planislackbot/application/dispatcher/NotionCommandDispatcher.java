package com.chatbot.plani.planislackbot.application.dispatcher;

import com.chatbot.plani.planislackbot.adapter.in.web.slack.dto.SlackBlockActionDTO;
import com.chatbot.plani.planislackbot.adapter.in.web.slack.dto.SlackEventCallbackDTO;
import com.chatbot.plani.planislackbot.application.assembler.SlackEventAssembler;
import com.chatbot.plani.planislackbot.application.port.in.NotionEventHandler;
import com.chatbot.plani.planislackbot.application.port.in.NotionInteractionHandler;
import com.chatbot.plani.planislackbot.domain.notion.enums.NotionDbIntent;
import com.chatbot.plani.planislackbot.domain.slack.vo.SlackCommandVO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 1. Notion 2차 intent 분배기
 * ex) meeting(회의), document(문서), memeber(멤버), vacation(휴가) 에 따라 NotionEventHandler 매핑
 *
 * 2. 인터랙션의 action_id 분배기
 * ex) NotionInteractionHandler
 */
@Service
public class NotionCommandDispatcher {

    // 노션 2차 intent (
    private final Map<NotionDbIntent, NotionEventHandler> handlerMap;
    private final Map<String, NotionInteractionHandler> interactionHandlerMap;

    public NotionCommandDispatcher(Map<NotionDbIntent, NotionEventHandler> handlerMap,
                                   @Qualifier("interactionHandlerMap")
                                   Map<String, NotionInteractionHandler> interactionHandlerMap) {

        this.handlerMap = handlerMap;
        this.interactionHandlerMap = interactionHandlerMap;
    }

    // Notion intent(회의, 휴가, 멤버, 문서 등) 핸들러로 2차 분기
    public void dispatch(SlackEventCallbackDTO slackEvent, NotionDbIntent dbIntent) {

        SlackCommandVO slackCommandVO = SlackEventAssembler.fromSlackEvent(slackEvent);
        NotionEventHandler handler = handlerMap.get(dbIntent);

        if (handler != null) {
            handler.handle(slackCommandVO);
        }
    }


    /**
     * Slack의 Notion 관련 인터랙션(예: 요약 버튼 클릭 등) actionId에 맞는 핸들러로 위임
     * <p>
     * - 블록 액션(action_id) 기반 분기
     * - 예: action_id = notion:summarize_page → MeetingSummaryInteractionHandler
     * - 인터랙션이 추가될수록 handlerMap에 등록만 하면 됨
     *
     * @param slackAction SlackBlockActionDTO (슬랙에서 들어온 블록 액션 payload)
     * @param actionId notion 의 어떤 액션인지 확인
     */
    public void dispatchInteraction(SlackBlockActionDTO slackAction, String actionId) {
        NotionInteractionHandler handler = interactionHandlerMap.get(actionId);
        if (handler != null) {
            handler.handleInteraction(slackAction);
        }
    }
}
