package com.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class CompanySection extends Section{
    @Serial
    private static final long serialVersionUID = 1L;
    private List<Company> companies;

    public CompanySection() {
    }

    @Override
    public String getTextRepresentation() {
        if (companies.isEmpty()) {
            return "Нет компаний";
        }
        return companies.stream()
                .map(Company::getNameCompany) 
                .reduce((first, second) -> first + ", " + second)
                .orElse("");
    }

    public CompanySection(Company company) {
        this.companies = new ArrayList<>();
    }

    public CompanySection(List<Company> organizations) {
        this.companies = organizations;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        CompanySection that = (CompanySection) o;
        return Objects.equals(companies, that.companies);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(companies);
    }

    @Override
    public List<Company> getContent() {
        return companies;
    }
}
