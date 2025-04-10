package com.stofka.upbitservice.candle.repository;

import com.stofka.upbitservice.candle.DayCandles;
import com.stofka.upbitservice.candle.MinuteCandles;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface DayCandleRepository extends Repository<DayCandles, Long> {

    @Query(value = """
        SELECT *
        FROM day_candles
        WHERE (code, candle_time) IN (
            SELECT code, MAX(candle_time)
            FROM day_candles
            GROUP BY code
        )
    """, nativeQuery = true)
    List<DayCandles> findLatestCandles();
}
