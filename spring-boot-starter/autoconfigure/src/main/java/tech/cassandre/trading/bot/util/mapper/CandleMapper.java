package tech.cassandre.trading.bot.util.mapper;

import org.knowm.xchange.dto.marketdata.CandleStick;
import org.knowm.xchange.dto.marketdata.CandleStickData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.cassandre.trading.bot.domain.ImportedCandle;
import tech.cassandre.trading.bot.dto.market.CandleDTO;
import tech.cassandre.trading.bot.dto.util.CurrencyPairDTO;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Candle mapper.
 */
@Mapper(uses = {CurrencyMapper.class})
public interface CandleMapper {

    /** Logger instance. */
    @SuppressWarnings("checkstyle:ConstantName")
    Logger logger = LoggerFactory.getLogger(CandleMapper.class);
    /** Currency mapper instance. */
    @SuppressWarnings("checkstyle:ConstantName")
    CurrencyMapper currencyMapper = Mappers.getMapper(CurrencyMapper.class);


    // =================================================================================================================
    // Domain to DTO.

    @Mapping(source = "currencyPairDTO", target = "currencyPair")
    CandleDTO mapToCandleDTO(ImportedCandle source);

    @SuppressWarnings("checkstyle:AvoidInlineConditionals")
    default Set<CandleDTO> mapToCandleDTOSet(CandleStickData candleStickData) {
        CurrencyPairDTO currencyPairDTO = currencyMapper.mapToCurrencyPairDTO(candleStickData.getInstrument());
        List<CandleStick> candleSticks = candleStickData.getCandleSticks();

        return candleSticks.stream()
                .map(candleStick -> {
                    return CandleDTO.builder()
                            .currencyPair(currencyPairDTO)
                            .timestamp((candleStick.getTimestamp() == null ? null : ZonedDateTime.ofInstant(candleStick.getTimestamp().toInstant(), ZoneId.systemDefault())))
                            .open(candleStick.getOpen())
                            .high(candleStick.getHigh())
                            .low(candleStick.getLow())
                            .close(candleStick.getClose())
                            .volume(candleStick.getVolume()).build();
                })
                .peek(candleDTO -> logger.debug("Mapped candle: {}", candleDTO))
                .collect(Collectors.toSet());
    }
}
