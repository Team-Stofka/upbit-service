package com.stofka.upbitservice.Repository;

import com.stofka.upbitservice.Domain.FiveMinuteCandles;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FiveMinuteCandleRepository extends JpaRepository<FiveMinuteCandles, Long> {

    @Query("SELECT c FROM FiveMinuteCandles c WHERE c.code = :code ORDER BY c.candleTime DESC")
    List<FiveMinuteCandles> findLatestCandles(@Param("code") String code, Pageable pageable);

}
