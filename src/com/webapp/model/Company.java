package com.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Company implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String companyName;
    private final String website;
    private List<Period> periods = new ArrayList<>();

    public Company() {
        this.companyName = null;
        this.website = null;
        this.periods = new ArrayList<>();
    }

    public Company(String name, String url, Period... periods) {
        this(name, url, List.of(periods));
    }

    public Company(String name, String url, List<Period> periods) {
        this.companyName = Objects.requireNonNull(name, "name must not be null");
        this.website = Objects.requireNonNull(url, "website must not be null");
        this.periods = new ArrayList<>();
    }

    public Company(String name, String url, List<Period> periods, List<Period> additionalPeriods) {
        this.companyName = Objects.requireNonNull(name, "name must not be null");
        this.website = Objects.requireNonNull(url, "website must not be null");
        this.periods = List.copyOf(periods);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Company that = (Company) o;
        return Objects.equals(companyName, that.companyName) &&
                Objects.equals(website, that.website) &&
                Objects.equals(periods, that.periods);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(companyName);
        result = 31 * result + Objects.hashCode(website);
        result = 31 * result + Objects.hashCode(periods);
        return result;
    }

    @Override
    public String toString() {
        return "Company{" +
                "companyName='" + companyName + '\'' +
                ", website='" + website + '\'' +
                ", periods=" + periods +
                '}';
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

    public void addPeriod(Period period) {
        periods.add(period);
    }
}



