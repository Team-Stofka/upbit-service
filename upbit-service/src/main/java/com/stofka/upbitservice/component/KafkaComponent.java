package com.stofka.upbitservice.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stofka.upbitservice.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class KafkaComponent {

    private final SseService sseService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "${kafka.topic.ticker}", groupId = "${kafka.consumer}")
    public void listenTicker(String message) {
        try {
            Map<String, Object> json = objectMapper.readValue(message, Map.class);
            sseService.broadcastToAllTickerClients(json); // ✅ 그대로 전송
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "${kafka.topic.candle}", groupId = "${kafka.consumer}")
    public void listenCandle(String message) {
        try {
            Map<String, Object> json = objectMapper.readValue(message, Map.class);
            sseService.sendCandleData(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
