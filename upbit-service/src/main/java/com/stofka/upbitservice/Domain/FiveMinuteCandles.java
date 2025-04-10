package com.stofka.upbitservice.Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "five_minute_candles")
public class FiveMinuteCandles extends Candle {

}
