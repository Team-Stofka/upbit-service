package com.stofka.upbitservice.Repository;

import com.stofka.upbitservice.Domain.TenMinuteCandles;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TenMinuteCandleRepository extends JpaRepository<TenMinuteCandles, Long> {

    @Query("SELECT c FROM TenMinuteCandles c WHERE c.code = :code ORDER BY c.candleTime DESC")
    List<TenMinuteCandles> findLatestCandles(@Param("code") String code, Pageable pageable);

}
