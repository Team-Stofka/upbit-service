package com.stofka.upbitservice.candle;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "minute_candles", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"code", "candle_time"})
})
public class MinuteCandles implements CandleBase{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @Column(name = "candle_time")
    private LocalDateTime candleTime;

    @Column(name = "opening_price")
    private BigDecimal openingPrice;

    @Column(name = "high_price")
    private BigDecimal highPrice;

    @Column(name = "low_price")
    private BigDecimal lowPrice;

    @Column(name = "trade_price")
    private BigDecimal tradePrice;

    @Column(name = "candle_acc_trade_volume")
    private BigDecimal candleAccTradeVolume;

    @Column(name = "candle_acc_trade_price")
    private BigDecimal candleAccTradePrice;
}
