package com.stofka.upbitservice.Repository;

import com.stofka.upbitservice.Domain.MinuteCandles;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MinuteCandleRepository extends JpaRepository<MinuteCandles, Long> {

    @Query("SELECT c FROM MinuteCandles c WHERE c.code = :code ORDER BY c.candleTime DESC")
    List<MinuteCandles> findLatestCandles(@Param("code") String code, Pageable pageable);

}
