package com.stofka.upbitservice.candle.repository;

import com.stofka.upbitservice.candle.MinuteCandles;
import com.stofka.upbitservice.candle.ThirtyMinuteCandles;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ThirtyMinuteCandleRepository extends Repository<ThirtyMinuteCandles, Long> {

    @Query(value = """
        SELECT *
        FROM thirty_minute_candles
        WHERE (code, candle_time) IN (
            SELECT code, MAX(candle_time)
            FROM thirty_minute_candles
            GROUP BY code
        )
    """, nativeQuery = true)
    List<ThirtyMinuteCandles> findLatestCandles();

    @Query("SELECT c FROM ThirtyMinuteCandles c WHERE c.code = :code ORDER BY c.candleTime DESC")
    List<ThirtyMinuteCandles> findLatestCandles(@Param("code") String code, Pageable pageable);
}
