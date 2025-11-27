package ru.skillbox.currency.exchange.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.currency.exchange.util.DecimalAdapter;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

//@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", sequenceName = "create_sequence", allocationSize = 0)
    @Column(name = "id")
    @XmlTransient
    private Long id;

    @Column(name = "name")
    @XmlElement(name = "Name")
    private String name;

    @Column(name = "nominal")
    @XmlElement(name = "Nominal")
    private Long nominal;

    @Column(name = "value")
    @XmlElement(name = "Value")
    @XmlJavaTypeAdapter(DecimalAdapter.class)
    private Double value;

    @Column(name = "iso_num_code")
    @XmlElement(name = "NumCode")
    private Long isoNumCode;

    @Column(name = "iso_letter_code")
    @XmlElement(name = "CharCode")
    private String isoLetterCode;
}
