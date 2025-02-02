package tech.cassandre.trading.bot.dto.market;

import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Getter
public enum CandlePeriodTypeDTO {

    /** 15 minutes. */
    CANDLE_15_MIN("15M", 15),
    /** 1 hour. */
    CANDLE_1_HOUR("1H", 60);

    /** Unique key. */
    private final String key;
    /** Period in milliseconds. */
    private final long periodInMillis;

    @SuppressWarnings("checkstyle:HiddenField")
    CandlePeriodTypeDTO(final String key, final long periodInMinutes) {
        this.key = key;
        this.periodInMillis = TimeUnit.MINUTES.toMillis(periodInMinutes);
    }
}
