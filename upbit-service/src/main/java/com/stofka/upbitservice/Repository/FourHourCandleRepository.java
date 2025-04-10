package com.stofka.upbitservice.Repository;

import com.stofka.upbitservice.Domain.FourHourCandles;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FourHourCandleRepository extends JpaRepository<FourHourCandles, Long> {

    @Query("SELECT c FROM FourHourCandles c WHERE c.code = :code ORDER BY c.candleTime DESC")
    List<FourHourCandles> findLatestCandles(@Param("code") String code, Pageable pageable);

}
