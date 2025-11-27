package ru.skillbox.currency.exchange.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.math.BigDecimal;

public class DecimalAdapter extends XmlAdapter<String, Double> {
    @Override
    public Double unmarshal(String v) throws Exception {
        if (v == null || v.isEmpty()) return null;
        // Заменяем запятую на точку для BigDecimal
        String normalized = v.trim().replace(',', '.');
        return new Double(normalized);
    }

    @Override
    public String marshal(Double aDouble) throws Exception {
        return "";
    }

}