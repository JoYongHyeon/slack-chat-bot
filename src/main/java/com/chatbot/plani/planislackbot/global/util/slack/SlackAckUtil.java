package com.chatbot.plani.planislackbot.global.util.slack;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class SlackAckUtil {

    private SlackAckUtil() {}

    /** block_actions 등: 빈 200 */
    public static ResponseEntity<String> empty() {
        return ResponseEntity.ok().build();
    }

    // 1) 모달 닫기 (가장 자주 쓰임) — 캐시 가능
    private static final ResponseEntity<String> CLEAR_ACK =
            ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                    .body("{\"response_action\":\"clear\"}");

    public static ResponseEntity<String> clear() {
        return CLEAR_ACK;
    }

    // 2) 필드 에러 표시 (action_id -> error msg)
    public static ResponseEntity<String> errors(Map<String, String> fieldErrors, ObjectMapper om) {
        ObjectNode root = om.createObjectNode();
        root.put("response_action", "errors");
        ObjectNode errors = root.putObject("errors");
        fieldErrors.forEach(errors::put);
        return json(root.toString());
    }

    // 3) 현재 모달 내용을 교체
    public static ResponseEntity<String> update(String viewJson) {
        String body = "{\"response_action\":\"update\",\"view\":" + viewJson + "}";
        return json(body);
    }

    // 4) 새 모달을 푸시
    public static ResponseEntity<String> push(String viewJson) {
        String body = "{\"response_action\":\"push\",\"view\":" + viewJson + "}";
        return json(body);
    }

    // 공통: JSON 200 OK
    private static ResponseEntity<String> json(String body) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(body);
    }

}
