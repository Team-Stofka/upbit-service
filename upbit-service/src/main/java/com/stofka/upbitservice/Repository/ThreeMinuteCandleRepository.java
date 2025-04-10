package com.stofka.upbitservice.Repository;

import com.stofka.upbitservice.Domain.ThreeMinuteCandles;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ThreeMinuteCandleRepository extends JpaRepository<ThreeMinuteCandles, Long> {

    @Query("SELECT c FROM ThreeMinuteCandles c WHERE c.code = :code ORDER BY c.candleTime DESC")
    List<ThreeMinuteCandles> findLatestCandles(@Param("code") String code, Pageable pageable);

}
