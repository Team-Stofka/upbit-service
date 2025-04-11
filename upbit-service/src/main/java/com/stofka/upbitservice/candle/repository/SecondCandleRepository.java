package com.stofka.upbitservice.candle.repository;

import com.stofka.upbitservice.candle.SecondCandle;
import com.stofka.upbitservice.candle.SecondCandleId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface SecondCandleRepository extends JpaRepository<SecondCandle, SecondCandleId>{

    @Query("SELECT c FROM SecondCandle c WHERE c.id.code = :code ORDER BY c.id.timestamp DESC")
    List<SecondCandle> findLatestCandles(String code, Pageable pageable);
}