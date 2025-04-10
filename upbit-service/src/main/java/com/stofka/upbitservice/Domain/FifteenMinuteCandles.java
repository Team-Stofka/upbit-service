package com.stofka.upbitservice.Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "fifteen_minute_candles")
public class FifteenMinuteCandles extends Candle {

}
