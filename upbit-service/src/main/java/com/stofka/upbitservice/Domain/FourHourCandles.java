package com.stofka.upbitservice.Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "four_hour_candles")
public class FourHourCandles extends Candle {

}
