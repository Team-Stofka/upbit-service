package com.stofka.upbitservice.Repository;

import com.stofka.upbitservice.Domain.ThirtyMinuteCandles;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ThirtyMinuteCandleRepository extends JpaRepository<ThirtyMinuteCandles, Long> {

    @Query("SELECT c FROM ThirtyMinuteCandles c WHERE c.code = :code ORDER BY c.candleTime DESC")
    List<ThirtyMinuteCandles> findLatestCandles(@Param("code") String code, Pageable pageable);

}
