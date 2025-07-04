package com.stofka.upbitservice.candle.repository;

import com.stofka.upbitservice.candle.MinuteCandles;
import com.stofka.upbitservice.candle.TenMinuteCandles;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TenMinuteCandleRepository extends Repository<TenMinuteCandles, Long> {

    @Query(value = """
        SELECT *
        FROM ten_minute_candles
        WHERE (code, candle_time) IN (
            SELECT code, MAX(candle_time)
            FROM ten_minute_candles
            GROUP BY code
        )
    """, nativeQuery = true)
    List<TenMinuteCandles> findLatestCandles();

    @Query("SELECT c FROM TenMinuteCandles c WHERE c.code = :code ORDER BY c.candleTime DESC")
    List<TenMinuteCandles> findLatestCandles(@Param("code") String code, Pageable pageable);
}
