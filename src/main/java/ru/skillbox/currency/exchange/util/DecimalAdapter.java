package ru.skillbox.currency.exchange.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DecimalAdapter extends XmlAdapter<String, Double> {
    @Override
    public Double unmarshal(String v) {
        if (v == null || v.isEmpty()) return null;
        String normalized = v.trim().replace(',', '.');
        return Double.valueOf(normalized);
    }

    @Override
    public String marshal(Double aDouble) {
        return "";
    }

}