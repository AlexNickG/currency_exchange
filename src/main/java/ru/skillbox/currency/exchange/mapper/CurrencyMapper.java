package ru.skillbox.currency.exchange.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.currency.exchange.dto.CurrencyDto;
import ru.skillbox.currency.exchange.dto.CurrencyResponseList;
import ru.skillbox.currency.exchange.dto.CurrencyShortDto;
import ru.skillbox.currency.exchange.entity.Currency;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {

    CurrencyDto convertToDto(Currency currency);

    Currency convertToEntity(CurrencyDto currencyDto);

    List<CurrencyShortDto> currencyToCurrencyShortDto(List<Currency> currencyList);

    default CurrencyResponseList currencyListToCurrencyResponseList(List<Currency> currencyList) {
        CurrencyResponseList responseList = new CurrencyResponseList();
        responseList.setCurrencies(currencyToCurrencyShortDto(currencyList));
        return responseList;
    }
}
