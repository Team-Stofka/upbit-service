package com.stofka.upbitservice.service;

import com.stofka.upbitservice.candle.SecondCandle;
import com.stofka.upbitservice.candle.repository.SecondCandleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SecondCandleService {
    private final SecondCandleRepository secondCandleRepository;
    private final RedisService redisService;

    public List<SecondCandle> getCandles(String code, String interval, int count) {
        if (!interval.equals("1s")) {
            throw new IllegalArgumentException("SecondCandleService only supports interval: 1s");
        }

        String redisKey = code + ":" + interval;
        Duration ttl = Duration.ofMinutes(30);

        List<SecondCandle> cached = redisService.getCandlesFromRedis(redisKey, SecondCandle.class, count);
        if (cached != null) return cached;

        var pageable = PageRequest.of(0, count);
        List<SecondCandle> candles = secondCandleRepository.findLatestCandles(code, pageable);
        redisService.saveCandlesToRedis(redisKey, candles, ttl);

        return candles;
    }
}