package com.stofka.upbitservice.component;

import com.stofka.upbitservice.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaComponent {

    private final SseService sseService;

    @KafkaListener(topics = "${kafka.topic.candle}", groupId = "${kafka.consumer}")
    public void listen(String message) {
        sseService.sendToAll(message);
    }

}
