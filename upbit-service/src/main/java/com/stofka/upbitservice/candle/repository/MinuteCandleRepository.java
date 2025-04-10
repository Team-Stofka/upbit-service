package com.stofka.upbitservice.candle.repository;

import com.stofka.upbitservice.candle.MinuteCandles;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.sql.Timestamp;
import java.util.List;

public interface MinuteCandleRepository extends Repository<MinuteCandles, Long> {

    @Query(value = """
        SELECT *
        FROM minute_candles
        WHERE (code, candle_time) IN (
            SELECT code, MAX(candle_time)
            FROM minute_candles
            GROUP BY code
        )
    """, nativeQuery = true)
    List<MinuteCandles> findLatestCandles();
}
