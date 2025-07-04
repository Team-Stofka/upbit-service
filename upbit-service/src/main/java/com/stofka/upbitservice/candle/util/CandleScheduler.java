package com.stofka.upbitservice.candle.util;

import com.stofka.upbitservice.candle.CandleBase;
import com.stofka.upbitservice.candle.repository.*;
import com.stofka.upbitservice.service.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CandleScheduler {

    private final MinuteCandleRepository minuteCandleRepository;
    private final ThreeMinuteCandleRepository threeMinuteCandleRepository;
    private final FiveMinuteCandleRepository fiveMinuteCandleRepository;
    private final TenMinuteCandleRepository tenMinuteCandleRepository;
    private final FifteenMinuteCandleRepository fifteenMinuteCandleRepository;
    private final ThirtyMinuteCandleRepository thirtyMinuteCandleRepository;
    private final HourCandleRepository hourCandleRepository;
    private final FourHourCandleRepository fourHourCandleRepository;
    private final DayCandleRepository dayCandleRepository;
    private final SseService sseService;

    @Scheduled(cron = "5 * * * * *") // 매 1분 5초
    public void send1Min() {
        send("candle.1m");
    }

    @Scheduled(cron = "5 */3 * * * *") // 매 3분 5초
    public void send3Min() {
        send("candle.3m");
    }

    @Scheduled(cron = "5 */5 * * * *") // 매 5분 5초
    public void send5Min() {
        send("candle.5m");
    }

    @Scheduled(cron = "5 */10 * * * *") // 매 10분 5초
    public void send10Min() {
        send("candle.10m");
    }

    @Scheduled(cron = "5 */15 * * * *") // 매 15분 5초
    public void send15Min() {
        send("candle.15m");
    }

    @Scheduled(cron = "5 */30 * * * *") // 매 30분 5초
    public void send30Min() {
        send("candle.30m");
    }

    @Scheduled(cron = "5 0 * * * *") // 매 시간 정각 5초
    public void send1Hour() {
        send("candle.1h");
    }

    @Scheduled(cron = "5 0 */4 * * *") // 매 4시간 정각 5초
    public void send4Hour() {
        send("candle.4h");
    }

    @Scheduled(cron = "5 0 0 * * *") // 매일 자정 5초
    public void send1Day() {
        send("candle.1d");
    }

    private void send(String interval) {
        List<? extends CandleBase> candles;

        switch (interval) {
            case "candle.1m" -> candles = minuteCandleRepository.findLatestCandles();
            case "candle.3m" -> candles = threeMinuteCandleRepository.findLatestCandles();
            case "candle.5m" -> candles = fiveMinuteCandleRepository.findLatestCandles();
            case "candle.10m" -> candles = tenMinuteCandleRepository.findLatestCandles();
            case "candle.15m" -> candles = fifteenMinuteCandleRepository.findLatestCandles();
            case "candle.30m" -> candles = thirtyMinuteCandleRepository.findLatestCandles();
            case "candle.1h" -> candles = hourCandleRepository.findLatestCandles();
            case "candle.4h" -> candles = fourHourCandleRepository.findLatestCandles();
            case "candle.1d" -> candles = dayCandleRepository.findLatestCandles();
            default -> {
                log.warn("Unknown interval: {}", interval);
                return;
            }
        }

        for (CandleBase candle : candles) {
            Map<String, Object> payload = Map.of(
                    "code", candle.getCode(),
                    "type", interval,
                    "timestamp", candle.getCandleTime(),
                    "opening_price", candle.getOpeningPrice(),
                    "high_price", candle.getHighPrice(),
                    "low_price", candle.getLowPrice(),
                    "trade_price", candle.getTradePrice(),
                    "candle_acc_trade_volume", candle.getCandleAccTradeVolume(),
                    "candle_acc_trade_price", candle.getCandleAccTradePrice()
            );
            sseService.sendCronCandleData(payload);
        }

        log.info("✅ Sent {}-interval candle data ({} items)", interval, candles.size());
    }
}
