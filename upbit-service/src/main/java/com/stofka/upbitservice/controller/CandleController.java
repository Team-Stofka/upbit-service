package com.stofka.upbitservice.controller;

import com.stofka.upbitservice.service.CandleService;
import com.stofka.upbitservice.service.SecondCandleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/candles")
@RequiredArgsConstructor
public class CandleController {

    private final CandleService candleService;
    private final SecondCandleService secondCandleService;

    @GetMapping
    public List<?> getCandles(
            @RequestParam String code,
            @RequestParam String interval,
            @RequestParam int count) {

        if(interval.equals("1s")) {
            return secondCandleService.getCandles(code, interval, count);
        } else {
            return candleService.getCandles(code, interval, count);
        }
    }
}