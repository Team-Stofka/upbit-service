package com.stofka.upbitservice.candle.repository;

import com.stofka.upbitservice.candle.FifteenMinuteCandles;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FifteenMinuteCandleRepository extends Repository<FifteenMinuteCandles, Long> {

    @Query(value = """
        SELECT *
        FROM fifteen_minute_candles
        WHERE (code, candle_time) IN (
            SELECT code, MAX(candle_time)
            FROM fifteen_minute_candles
            GROUP BY code
        )
    """, nativeQuery = true)
    List<FifteenMinuteCandles> findLatestCandles();

    @Query("SELECT c FROM FifteenMinuteCandles c WHERE c.code = :code ORDER BY c.candleTime DESC")
    List<FifteenMinuteCandles> findLatestCandles(@Param("code") String code, Pageable pageable);
}
