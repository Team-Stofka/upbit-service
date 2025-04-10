package com.stofka.upbitservice.Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public abstract class Candle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String code;

    @Column(name = "candle_time", nullable = false)
    private LocalDateTime candleTime;

    private BigDecimal openingPrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal tradePrice;
    private BigDecimal candleAccTradeVolume;
    private BigDecimal candleAccTradePrice;
}
