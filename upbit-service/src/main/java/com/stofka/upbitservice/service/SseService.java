package com.stofka.upbitservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseService {

    // ✅ 종목별 Emitter 리스트 (candle 전용)
    private final Map<String, List<SseEmitter>> candleEmitters = new ConcurrentHashMap<>();

    // ✅ 전체 ticker emitter 리스트
    private final List<SseEmitter> globalTickerEmitters = new CopyOnWriteArrayList<>();

    // ✅ candle용 종목별 연결
    public SseEmitter connect(String code) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        candleEmitters.computeIfAbsent(code, k -> new CopyOnWriteArrayList<>()).add(emitter);

        emitter.onCompletion(() -> candleEmitters.getOrDefault(code, new ArrayList<>()).remove(emitter));
        emitter.onTimeout(() -> candleEmitters.getOrDefault(code, new ArrayList<>()).remove(emitter));
        emitter.onError(e -> candleEmitters.getOrDefault(code, new ArrayList<>()).remove(emitter));

        log.info("SSE candle connected: {}", code);
        return emitter;
    }

    // ✅ ticker용 전체 연결
    public SseEmitter connectAllTicker() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        globalTickerEmitters.add(emitter);

        emitter.onCompletion(() -> globalTickerEmitters.remove(emitter));
        emitter.onTimeout(() -> globalTickerEmitters.remove(emitter));
        emitter.onError(e -> globalTickerEmitters.remove(emitter));

        log.info("SSE ticker connected");
        return emitter;
    }

    // ✅ ticker 메시지 전체 브로드캐스트 (가공 없이)
    public void broadcastToAllTickerClients(Map<String, Object> kafkaMessage) {
        for (SseEmitter emitter : new ArrayList<>(globalTickerEmitters)) {
            try {
                emitter.send(SseEmitter.event()
                        .name("ticker-data")
                        .data(kafkaMessage));
            } catch (IOException | IllegalStateException e) {
                emitter.completeWithError(e); // 안전하게 종료
                globalTickerEmitters.remove(emitter); // ❗ 명시적으로 제거
                log.warn("Removed closed SSE connection (ticker): {}", e.getMessage());
            }
        }
    }

    // ✅ kafka candle 메시지 전송 (가공 포함, 종목별로)
    public void sendCandleData(Map<String, Object> kafkaMessage) {
        Map<String, Object> payload = (Map<String, Object>) kafkaMessage.get("payload");
        if (payload == null || payload.get("code") == null) return;

        String coinCode = ((String) payload.get("code"));
        Map<String, Object> transformed = transformCandlePayload(payload);

        List<SseEmitter> emitters = candleEmitters.getOrDefault(coinCode, Collections.emptyList());
        for (SseEmitter emitter : new ArrayList<>(emitters)) {
            try {
                emitter.send(SseEmitter.event()
                        .name("candle-data")
                        .data(Map.of("payload", transformed)));
            } catch (IOException | IllegalStateException e) {
                emitter.completeWithError(e);
                candleEmitters.get(coinCode).remove(emitter); // ❗ 제거
                log.warn("Removed closed SSE connection (candle): {}", e.getMessage());
            }
        }
    }

    // postgre candle 데이터 전송
    public void sendCronCandleData(Map<String, Object> payload) {
        String coinCode = (String) payload.get("code");
        List<SseEmitter> emitters = candleEmitters.getOrDefault(coinCode, Collections.emptyList());

        for (SseEmitter emitter : new ArrayList<>(emitters)) {
            try {
                emitter.send(SseEmitter.event()
                        .name("postgres-candle-data")
                        .data(Map.of("payload", payload)));
            } catch (IOException | IllegalStateException e) {
                emitter.completeWithError(e);
                candleEmitters.get(coinCode).remove(emitter);
                log.warn("SSE 제거됨 (cron): {}", e.getMessage());
            }
        }
    }

    private Map<String, Object> transformCandlePayload(Map<String, Object> payload) {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "candle.1s");
        result.put("code", (payload.get("code")));
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

