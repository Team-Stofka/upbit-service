package com.stofka.upbitservice.controller;

import com.stofka.upbitservice.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stream")
public class TickerStreamController {

    private final SseService sseService;

    @GetMapping(value = "/ticker", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamTicker() {
        return sseService.connectAllTicker();
    }
}

