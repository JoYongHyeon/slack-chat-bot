package com.chatbot.plani.planislackbot.global.util.slack;


import com.slack.api.model.block.Blocks;
import com.slack.api.model.block.InputBlock;
import com.slack.api.model.block.composition.BlockCompositions;
import com.slack.api.model.block.composition.OptionObject;
import com.slack.api.model.block.composition.PlainTextObject;
import com.slack.api.model.block.element.BlockElements;
import com.slack.api.model.block.element.PlainTextInputElement;
import com.slack.api.model.block.element.StaticSelectElement;
import com.slack.api.model.view.View;
import com.slack.api.model.view.ViewClose;
import com.slack.api.model.view.ViewSubmit;
import com.slack.api.model.view.ViewTitle;

import java.util.Arrays;

import static com.chatbot.plani.planislackbot.global.util.constant.slack.SlackConstant.VACATION_REGISTER_MODAL_ID;

/**
 * 휴가 관리 DB 맞춤 Slack 모달 생성 유틸
 * - 유형: select
 * - 시작일/종료일: datePicker
 * - 상태: select
 * - 담당자: select
 * - 사유: multiline text input
 */
public class SlackModalUtil {

    private SlackModalUtil() {
    }

    ;


    /**
     * 휴가 등록 모달 View
     * - callbackId는 제출(view_submission) 라우팅에 사용.
     */
    public static View vacationRegisterModal() {
        return View.builder()
                .type("modal")
                .callbackId(VACATION_REGISTER_MODAL_ID)
                .title(ViewTitle.builder().type("plain_text").text("휴가 등록").build())
                .submit(ViewSubmit.builder().type("plain_text").text("등록").build())
                .close(ViewClose.builder().type("plain_text").text("취소").build())
                .blocks(Blocks.asBlocks(

                        // 신청자 (plain_text_input)
                        Blocks.input(i -> i
                                .blockId("applicant_block")
                                .element(PlainTextInputElement.builder()
                                        .actionId("applicant_input")      // ← formData 키로 쓰일 action_id
                                        .placeholder(PlainTextObject.builder().text("신청자 이름").build())
                                        .build())
                                .label(BlockCompositions.plainText("신청자"))
                        ),

                        // 유형 선택
                        Blocks.input(i -> i
                                .blockId("type_block")
                                .element(BlockElements.staticSelect(s -> s
                                        .actionId("type_input")
                                        .placeholder(BlockCompositions.plainText("유형 선택"))
                                        .options(Arrays.asList(
                                                OptionObject.builder()
                                                        .text(PlainTextObject.builder().text("연차").build())
                                                        .value("연차")
                                                        .build(),
                                                OptionObject.builder()
                                                        .text(PlainTextObject.builder().text("오전반차").build())
                                                        .value("오전반차")
                                                        .build(),
                                                OptionObject.builder()
                                                        .text(PlainTextObject.builder().text("오후반차").build())
                                                        .value("오후반차")
                                                        .build(),
                                                OptionObject.builder()
                                                        .text(PlainTextObject.builder().text("병가").build())
                                                        .value("병가")
                                                        .build(),
                                                OptionObject.builder()
                                                        .text(PlainTextObject.builder().text("기타").build())
                                                        .value("기타")
                                                        .build()
                                        ))
                                ))
                                .label(BlockCompositions.plainText("유형"))
                        ),

                        // 시작일
                        Blocks.input(i -> i
                                .blockId("start_date_block")
                                .element(BlockElements.datePicker(d -> d.actionId("start_date_input")))
                                .label(BlockCompositions.plainText("시작일"))
                        ),

                        // 종료일
                        Blocks.input(i -> i
                                .blockId("end_date_block")
                                .element(BlockElements.datePicker(d -> d.actionId("end_date_input")))
                                .label(BlockCompositions.plainText("종료일"))
                        ),

                        // 상태 선택
                        InputBlock.builder()
                                .blockId("status_block")
                                .label(PlainTextObject.builder().text("상태").build())
                                .element(StaticSelectElement.builder()
                                        .actionId("status_action")
                                        .placeholder(PlainTextObject.builder().text("상태 선택").build())
                                        .options(Arrays.asList(
                                                OptionObject.builder().text(PlainTextObject.builder().text("신청").build()).value("신청").build(),
                                                OptionObject.builder().text(PlainTextObject.builder().text("승인").build()).value("승인").build(),
                                                OptionObject.builder().text(PlainTextObject.builder().text("거절").build()).value("거절").build()
                                        ))
                                        .build())
                                .build(),

                        // 5. 담당자 선택
                        InputBlock.builder()
                                .blockId("manager_block")
                                .label(PlainTextObject.builder().text("담당자").build())
                                .element(StaticSelectElement.builder()
                                        .actionId("manager_action")
                                        .placeholder(PlainTextObject.builder().text("담당자 선택").build())
                                        .options(Arrays.asList(
                                                OptionObject.builder().text(PlainTextObject.builder().text("권장환").build()).value("권장환").build(),
                                                OptionObject.builder().text(PlainTextObject.builder().text("김은화").build()).value("김은화").build(),
                                                OptionObject.builder().text(PlainTextObject.builder().text("황동훈").build()).value("황동훈").build(),
                                                OptionObject.builder().text(PlainTextObject.builder().text("원미정").build()).value("원미정").build(),
                                                OptionObject.builder().text(PlainTextObject.builder().text("윤서진").build()).value("윤서진").build(),
                                                OptionObject.builder().text(PlainTextObject.builder().text("김승원").build()).value("김승원").build(),
                                                OptionObject.builder().text(PlainTextObject.builder().text("김연규").build()).value("김연규").build(),
                                                OptionObject.builder().text(PlainTextObject.builder().text("손민구").build()).value("손민구").build(),
                                                OptionObject.builder().text(PlainTextObject.builder().text("장부근").build()).value("장부근").build(),
                                                OptionObject.builder().text(PlainTextObject.builder().text("안수빈").build()).value("안수빈").build(),
                                                OptionObject.builder().text(PlainTextObject.builder().text("조용현").build()).value("조용현").build(),
                                                OptionObject.builder().text(PlainTextObject.builder().text("권다애").build()).value("권다애").build(),
                                                OptionObject.builder().text(PlainTextObject.builder().text("정성훈").build()).value("정성훈").build(),
                                                OptionObject.builder().text(PlainTextObject.builder().text("정헌기").build()).value("정헌기").build(),
                                                OptionObject.builder().text(PlainTextObject.builder().text("설동비").build()).value("설동비").build(),
                                                OptionObject.builder().text(PlainTextObject.builder().text("박명희").build()).value("박명희").build(),
                                                OptionObject.builder().text(PlainTextObject.builder().text("이주연").build()).value("이주연").build()
                                        ))
                                        .build())
                                .build(),

                        // 6. 사유 입력
                        InputBlock.builder()
                                .blockId("reason_block")
                                .label(PlainTextObject.builder().text("사유").build())
                                .element(PlainTextInputElement.builder()
                                        .actionId("reason_action")
                                        .multiline(true)
                                        .placeholder(PlainTextObject.builder().text("사유를 입력하세요").build())
                                        .build())
                                .build()
                ))
                .build();
    }
}
