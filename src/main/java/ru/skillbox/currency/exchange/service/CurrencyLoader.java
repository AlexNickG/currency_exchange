package ru.skillbox.currency.exchange.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.skillbox.currency.exchange.entity.Currency;
import ru.skillbox.currency.exchange.dto.ValCurs;
import ru.skillbox.currency.exchange.repository.CurrencyRepository;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyLoader {

    @Value("${app.url}")
    private String cbrUrl;

    private final CurrencyRepository repository;

    private final CurrencyService service;


    @Scheduled(fixedRateString = "60", timeUnit = TimeUnit.MINUTES)
    public void updateCurrencyData() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String xmlData = restTemplate.getForObject (cbrUrl, String.class);
            JAXBContext context = JAXBContext.newInstance(ValCurs.class);
            if (xmlData == null) {
                throw new IOException();
            }
            ValCurs valCurs = (ValCurs) context.createUnmarshaller().unmarshal(new StringReader(xmlData));

            valCurs.getCurrencies().forEach(currency -> {
                Optional<Currency> existingCurrencyOpt = repository.findByIsoLetterCode(currency.getIsoLetterCode());
                existingCurrencyOpt.ifPresentOrElse(
                        existingCurrency -> service.update(currency, existingCurrency),
                        () -> repository.save(currency)
                );
            });
            log.info("Курс валют обновлен");
        } catch (JAXBException e) {
            log.error("Ошибка парсинга данных с сайта: {}", cbrUrl, e);
        } catch (IOException e) {
            log.error("Ошибка подключения к сайту: {}", cbrUrl, e);
        }
    }
}
