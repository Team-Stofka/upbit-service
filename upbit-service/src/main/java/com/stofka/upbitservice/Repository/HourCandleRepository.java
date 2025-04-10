package com.stofka.upbitservice.Repository;

import com.stofka.upbitservice.Domain.HourCandles;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HourCandleRepository extends JpaRepository<HourCandles, Long> {

    @Query("SELECT c FROM HourCandles c WHERE c.code = :code ORDER BY c.candleTime DESC")
    List<HourCandles> findLatestCandles(@Param("code") String code, Pageable pageable);

}
