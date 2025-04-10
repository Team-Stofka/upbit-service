package com.stofka.upbitservice.Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "ten_minute_candles")
public class TenMinuteCandles extends Candle {

}
