package com.stofka.upbitservice.candle.repository;

import com.stofka.upbitservice.candle.DayCandles;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

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

    @Query("SELECT c FROM DayCandles c WHERE c.code = :code ORDER BY c.candleTime DESC")
    List<DayCandles> findLatestCandles(@Param("code") String code, Pageable pageable);
}
