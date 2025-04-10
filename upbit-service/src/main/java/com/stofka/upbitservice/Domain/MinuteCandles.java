package com.stofka.upbitservice.Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "minute_candles")
public class MinuteCandles extends Candle {

}
