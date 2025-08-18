package com.chatbot.plani.planislackbot.application.service.slack.handler.event;

import com.chatbot.plani.planislackbot.application.port.in.NotionEventHandler;
import com.chatbot.plani.planislackbot.application.port.out.slack.SlackSendPort;
import com.chatbot.plani.planislackbot.domain.notion.enums.NotionDbIntent;
import com.chatbot.plani.planislackbot.domain.slack.vo.SlackCommandVO;
import com.chatbot.plani.planislackbot.global.util.notion.helper.NotionEventHandlerHelper;
import com.chatbot.plani.planislackbot.global.util.slack.SlackBlockUtil;
import com.slack.api.model.block.DividerBlock;
import com.slack.api.model.block.LayoutBlock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VacationRegisterEventHandler implements NotionEventHandler {

    private final NotionEventHandlerHelper notionEventHandlerHelper;
    private final SlackSendPort slackSendPort;

    @Override
    public NotionDbIntent getDbIntent() {
        return NotionDbIntent.VACATION_REGISTER;
    }

    @Override
    public void handle(SlackCommandVO commandVO) {

        // 1. 명령 / 키워드 / 검색결과 체크는 helper 에서 담당 (문제시 return)
        if (notionEventHandlerHelper.invalidCommand(commandVO, this.getClass().getName())) return;

        DividerBlock divider = DividerBlock.builder().build();
        LayoutBlock section = SlackBlockUtil.vacationRegisterOpenModalBlock();
        slackSendPort.sendBlocks(commandVO.channel(), List.of(divider, section), null);
    }
}
