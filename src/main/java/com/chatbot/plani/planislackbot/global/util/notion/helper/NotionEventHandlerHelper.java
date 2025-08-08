package com.chatbot.plani.planislackbot.global.util.notion.helper;


import com.chatbot.plani.planislackbot.application.port.out.slack.SlackSendPort;
import com.chatbot.plani.planislackbot.domain.slack.vo.SlackCommandVO;
import com.chatbot.plani.planislackbot.global.util.StringUtil;
import com.chatbot.plani.planislackbot.global.util.notion.NotionSearchUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static com.chatbot.plani.planislackbot.global.util.constant.slack.SlackConstant.*;

@Component
@Slf4j
// TODO: 모든 로그 공통 처리 필요
public class NotionEventHandlerHelper {

    /**
     * 명령 객체가 null일 경우 에러 로그를 남기고 true 반환
     * (채널 식별 불가 등 이상 요청 방지용)
     */
    public boolean invalidCommand(SlackCommandVO commandVO,
                                  String HandlerName) {
        if (commandVO == null) {
            log.error("[{}] : {}", ERROR_UNKNOWN_CHANEL, HandlerName);
            return true;
        }
        return false;
    }

    /**
     * 키워드가 비어 있을 경우 슬랙에 안내 메시지 전송 후 true 반환
     */
    public boolean emptyKeyword(SlackCommandVO commandVO,
                                SlackSendPort slackSendPort) {
        if (StringUtil.isEmpty(commandVO.keyword())) {
            slackSendPort.sendText(commandVO.channel(), ERROR_KEYWORD);
            return true;
        }
        return false;
    }

    /**
     * 검색 결과가 없을 경우 슬랙에 안내 메시지 전송 후 true 반환
     */
    public boolean emptySearchResult(Collection<?> searchResults,
                                     SlackSendPort slackSendPort,
                                     String channel) {

        if (NotionSearchUtil.isEmpty(searchResults)) {
            slackSendPort.sendText(channel, ERROR_NO_SUCH_NOTION_PAGE);
            return true;
        }
        return false;
    }
}
