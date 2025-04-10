package com.stofka.upbitservice.Repository;

import com.stofka.upbitservice.Domain.DayCandles;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DayCandleRepository extends JpaRepository<DayCandles, Long> {

    @Query("SELECT c FROM DayCandles c WHERE c.code = :code ORDER BY c.candleTime DESC")
    List<DayCandles> findLatestCandles(@Param("code") String code, Pageable pageable);

}
