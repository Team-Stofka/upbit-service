package com.stofka.upbitservice.Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
@Entity
@Table(name = "market_candle")
@Getter
@Setter
public class SecondCandle {
    @EmbeddedId
    private SecondCandleId id;

    @Column(name = "type")
    private String type;

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

    @Column(name = "stream_type")
    private String streamType;
}
