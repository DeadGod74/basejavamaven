package com.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Company implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String companyName;
    private final String website;
    private List<Period> periods;

    // Конструктор по умолчанию
    public Company() {
        this.companyName = null;
        this.website = null;
        this.periods = new ArrayList<>();
    }

    // Конструктор с двумя параметрами
    public Company(String name, String url) {
        this(name, url, new ArrayList<>()); // Создаем пустой список периодов
    }

    // Конструктор с тремя параметрами
    public Company(String name, String url, List<Period> periods) {
        this.companyName = Objects.requireNonNull(name, "name must not be null");
        this.website = Objects.requireNonNull(url, "website must not be null");
        this.periods = new ArrayList<>(periods);
    }

    // Метод для установки периодов
    public void setPeriods(List<Period> periods) {
        this.periods = new ArrayList<>(periods);
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public String getNameCompany() {
        return companyName;
    }

    public String getWebsite() {
        return website;
    }

}

