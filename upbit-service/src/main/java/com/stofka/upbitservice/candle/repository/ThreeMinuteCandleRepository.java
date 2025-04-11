package com.stofka.upbitservice.candle.repository;

import com.stofka.upbitservice.candle.MinuteCandles;
import com.stofka.upbitservice.candle.ThreeMinuteCandles;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ThreeMinuteCandleRepository extends Repository<ThreeMinuteCandles, Long> {

    @Query(value = """
        SELECT *
        FROM three_minute_candles
        WHERE (code, candle_time) IN (
            SELECT code, MAX(candle_time)
            FROM three_minute_candles
            GROUP BY code
        )
    """, nativeQuery = true)
    List<ThreeMinuteCandles> findLatestCandles();

    @Query("SELECT c FROM ThreeMinuteCandles c WHERE c.code = :code ORDER BY c.candleTime DESC")
    List<ThreeMinuteCandles> findLatestCandles(@Param("code") String code, Pageable pageable);
}
