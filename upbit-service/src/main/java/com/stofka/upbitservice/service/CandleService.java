package com.stofka.upbitservice.service;

import com.stofka.upbitservice.Domain.*;
import com.stofka.upbitservice.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CandleService {

    private final MinuteCandleRepository minuteCandleRepository;
    private final ThreeMinuteCandleRepository threeMinuteCandleRepository;
    private final FiveMinuteCandleRepository fiveMinuteCandleRepository;
    private final TenMinuteCandleRepository tenMinuteCandleRepository;
    private final FifteenMinuteCandleRepository fifteenMinuteCandleRepository;
    private final ThirtyMinuteCandleRepository thirtyMinuteCandleRepository;
    private final HourCandleRepository hourCandleRepository;
    private final FourHourCandleRepository fourHourCandleRepository;
    private final DayCandleRepository dayCandleRepository;
    private final RedisService redisService;


    public List<? extends Candle> getCandles(String code, String interval, int count) {
        String redisKey = code + ":" + interval;
        Duration ttl = Duration.ofMinutes(30);

        // Redis 먼저 조회
        List<? extends Candle> cached = switch (interval) {
            case "1m" -> redisService.getCandlesFromRedis(redisKey, MinuteCandles.class, count);
            case "3m" -> redisService.getCandlesFromRedis(redisKey, ThreeMinuteCandles.class, count);
            case "5m" -> redisService.getCandlesFromRedis(redisKey, FiveMinuteCandles.class, count);
            case "10m" -> redisService.getCandlesFromRedis(redisKey, TenMinuteCandles.class, count);
            case "15m" -> redisService.getCandlesFromRedis(redisKey, FifteenMinuteCandles.class, count);
            case "30m" -> redisService.getCandlesFromRedis(redisKey, ThirtyMinuteCandles.class, count);
            case "1h" -> redisService.getCandlesFromRedis(redisKey, HourCandles.class, count);
            case "4h" -> redisService.getCandlesFromRedis(redisKey, FourHourCandles.class, count);
            case "1d" -> redisService.getCandlesFromRedis(redisKey, DayCandles.class, count);

            default -> throw new IllegalArgumentException("Unsupported interval: " + interval);
        };

        if (cached != null) {
            return cached;
        }

        Pageable pageable = PageRequest.of(0, count);
        List<? extends Candle> candles = switch (interval) {
            case "1m" -> minuteCandleRepository.findLatestCandles(code, pageable);
            case "3m" -> threeMinuteCandleRepository.findLatestCandles(code, pageable);
            case "5m" -> fiveMinuteCandleRepository.findLatestCandles(code, pageable);
            case "10m" -> tenMinuteCandleRepository.findLatestCandles(code, pageable);
            case "15m" -> fifteenMinuteCandleRepository.findLatestCandles(code, pageable);
            case "30m" -> thirtyMinuteCandleRepository.findLatestCandles(code, pageable);
            case "1h" -> hourCandleRepository.findLatestCandles(code, pageable);
            case "4h" -> fourHourCandleRepository.findLatestCandles(code, pageable);
            case "1d" -> dayCandleRepository.findLatestCandles(code, pageable);

            default -> throw new IllegalArgumentException("Invalid interval: " + interval);
        };

        redisService.saveCandlesToRedis(redisKey, candles, ttl);

        return candles;
    }
}
