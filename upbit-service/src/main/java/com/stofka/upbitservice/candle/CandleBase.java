package com.stofka.upbitservice.candle;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface CandleBase {
    String getCode();
    LocalDateTime getCandleTime();
    BigDecimal getOpeningPrice();
    BigDecimal getHighPrice();
    BigDecimal getLowPrice();
    BigDecimal getTradePrice();
    BigDecimal getCandleAccTradeVolume();
    BigDecimal getCandleAccTradePrice();

}
