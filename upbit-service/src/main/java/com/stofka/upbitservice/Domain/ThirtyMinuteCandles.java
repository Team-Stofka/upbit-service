package com.stofka.upbitservice.Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "thirty_minute_candles")
public class ThirtyMinuteCandles extends Candle {

}
