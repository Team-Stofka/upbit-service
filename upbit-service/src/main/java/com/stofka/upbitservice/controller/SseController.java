package com.stofka.upbitservice.controller;

import com.stofka.upbitservice.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sse")
public class SseController {

    private final SseService sseService;

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect() {
        return sseService.connect();
    }

    @PostMapping("/send")
    public void sendMessage(@RequestBody String message) {
        sseService.sendToAll(message);
    }

}
