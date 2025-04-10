package com.stofka.upbitservice.Repository;

import com.stofka.upbitservice.Domain.SecondCandle;
import com.stofka.upbitservice.Domain.SecondCandleId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface SecondCandleRepository extends JpaRepository<SecondCandle, SecondCandleId>{

    @Query("SELECT c FROM SecondCandle c WHERE c.id.code = :code ORDER BY c.id.timestamp DESC")
    List<SecondCandle> findLatestCandles(String code, Pageable pageable);
}
