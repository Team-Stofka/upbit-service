package com.stofka.upbitservice.Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "three_minute_candles")
public class ThreeMinuteCandles extends Candle {

}
