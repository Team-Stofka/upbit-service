package com.stofka.upbitservice.Domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class SecondCandleId implements Serializable {
    private String code;
    private Long timestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SecondCandleId)) return false;
        SecondCandleId that = (SecondCandleId) o;
        return Objects.equals(code, that.code) && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, timestamp);
    }
}
