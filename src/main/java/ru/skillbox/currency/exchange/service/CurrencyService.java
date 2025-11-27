package ru.skillbox.currency.exchange.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.skillbox.currency.exchange.dto.CurrencyDto;
import ru.skillbox.currency.exchange.dto.CurrencyResponseList;
import ru.skillbox.currency.exchange.entity.Currency;
import ru.skillbox.currency.exchange.entity.ValCurs;
import ru.skillbox.currency.exchange.mapper.CurrencyMapper;
import ru.skillbox.currency.exchange.repository.CurrencyRepository;
import ru.skillbox.currency.exchange.util.BeanUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyMapper mapper;
    private final CurrencyRepository repository;

    @Value("${app.url}")
    private String cbrUrl;

    public CurrencyResponseList getAllCurrencies() {
        log.info("CurrencyService method getAllCurrencies executed");
        return mapper.currencyListToCurrencyResponseList(repository.findAll());
    }

    public CurrencyDto getById(Long id) {
        log.info("CurrencyService method getById executed");
        Currency currency = repository.findById(id).orElseThrow(() -> new RuntimeException("Currency not found with id: " + id));
        return mapper.convertToDto(currency);
    }

    public Double convertValue(Long value, Long numCode) {
        log.info("CurrencyService method convertValue executed");
        Currency currency = repository.findByIsoNumCode(numCode);
        return value * currency.getValue();
    }

    public CurrencyDto create(CurrencyDto dto) {
        log.info("CurrencyService method create executed");
        return mapper.convertToDto(repository.save(mapper.convertToEntity(dto)));
    }

    public void update(Currency currencyNew, Currency currency) {
        BeanUtils.copyNonNullProperties(currencyNew, currency);
        repository.save(currency);
    }

    @Scheduled(fixedRateString = "60", timeUnit = TimeUnit.MINUTES)
    public void updateCurrencyData() {
        try (var inputStream = new URL(cbrUrl).openStream()) {
            JAXBContext context = JAXBContext.newInstance(ValCurs.class);
            ValCurs valCurs = (ValCurs) context.createUnmarshaller().unmarshal(inputStream);

            valCurs.getCurrencies().forEach(currency -> {
                Optional<Currency> existingCurrencyOpt = repository.findByIsoLetterCode(currency.getIsoLetterCode());
                existingCurrencyOpt.ifPresentOrElse(
                        existingCurrency -> update(currency, existingCurrency),
                        () -> repository.save(currency)
                );
            });
            log.info("Курс валют обновлен");
        } catch (IOException e) {
            log.error("Ошибка подключения к сайту ЦБ: {}", cbrUrl, e);
        } catch (JAXBException e) {
            log.error("Ошибка парсинга данных с сайта ЦБ: {}", cbrUrl, e);
        }

    }
}
