package com.stofka.upbitservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseService {

    // 종목별 Emitter 리스트
    private final Map<String, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    // 클라이언트 연결 시 종목 코드로 emitter 등록
    public SseEmitter connect(String code) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        emitters.computeIfAbsent(code, k -> new ArrayList<>()).add(emitter);

        emitter.onCompletion(() -> removeEmitter(code, emitter));
        emitter.onTimeout(() -> removeEmitter(code, emitter));
        emitter.onError(e -> removeEmitter(code, emitter));

        log.info("SSE connected: {}", code);
        return emitter;
    }

    // emitter 제거
    private void removeEmitter(String code, SseEmitter emitter) {
        List<SseEmitter> list = emitters.get(code);
        if (list != null) {
            list.remove(emitter);
        }
    }

    // Kafka 메시지를 받아서 가공 후 전송
    public void sendData(Map<String, Object> kafkaMessage) {
        Map<String, Object> payload = (Map<String, Object>) kafkaMessage.get("payload");
        if (payload == null || payload.get("code") == null) return;

        // 예: BTC-USD → BTC
        String coinCode = ((String) payload.get("code")).split("-")[0];

        Map<String, Object> transformed = transformPayload(payload);

        List<SseEmitter> emitterList = emitters.getOrDefault(coinCode, new ArrayList<>());

        for (SseEmitter emitter : emitterList) {
            try {
                emitter.send(SseEmitter.event()
                        .name("coin-data")
                        .data(Map.of("payload", transformed)));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }
    }

    // payload 데이터 변환
    private Map<String, Object> transformPayload(Map<String, Object> payload) {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "candle.1s");
        result.put("code", ((String) payload.get("code")).split("-")[0]);
        result.put("opening_price", payload.get("opening_price"));
        result.put("high_price", payload.get("high_price"));
        result.put("low_price", payload.get("low_price"));
        result.put("trade_price", payload.get("trade_price"));
        result.put("candle_acc_trade_volume", payload.get("candle_acc_trade_volume"));
        result.put("candle_acc_trade_price", payload.get("candle_acc_trade_price"));
        result.put("timestamp", payload.get("timestamp"));
        result.put("stream_type", ((String) payload.get("stream_type")).toUpperCase());
        return result;
    }
}
