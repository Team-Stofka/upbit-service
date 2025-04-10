package com.stofka.upbitservice.Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "hour_candles")
public class HourCandles extends Candle {

}
