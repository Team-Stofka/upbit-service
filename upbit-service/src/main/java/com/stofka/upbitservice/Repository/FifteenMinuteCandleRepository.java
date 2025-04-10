package com.stofka.upbitservice.Repository;

import com.stofka.upbitservice.Domain.FifteenMinuteCandles;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FifteenMinuteCandleRepository extends JpaRepository<FifteenMinuteCandles, Long> {

    @Query("SELECT c FROM FifteenMinuteCandles c WHERE c.code = :code ORDER BY c.candleTime DESC")
    List<FifteenMinuteCandles> findLatestCandles(@Param("code") String code, Pageable pageable);

}
