package ru.skillbox.currency.exchange.dto;

import lombok.Getter;
import lombok.Setter;
import ru.skillbox.currency.exchange.entity.Currency;

import javax.xml.bind.annotation.*;
import java.util.List;

@Getter
@Setter
@XmlRootElement(name = "ValCurs")
@XmlAccessorType(XmlAccessType.FIELD)
public class ValCurs {

    @XmlAttribute(name = "Date")
    private String date;

    @XmlAttribute(name = "name")
    private String name;

    @XmlElement(name = "Valute")
    private List<Currency> currencies;
}
