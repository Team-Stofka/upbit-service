package com.stofka.upbitservice.candle.repository;

import com.stofka.upbitservice.candle.FourHourCandles;
import com.stofka.upbitservice.candle.MinuteCandles;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface FourHourCandleRepository extends Repository<FourHourCandles, Long> {

    @Query(value = """
        SELECT *
        FROM four_hour_candles
        WHERE (code, candle_time) IN (
            SELECT code, MAX(candle_time)
            FROM four_hour_candles
            GROUP BY code
        )
    """, nativeQuery = true)
    List<FourHourCandles> findLatestCandles();
}
