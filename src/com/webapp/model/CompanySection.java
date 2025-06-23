package com.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@XmlAccessorType(XmlAccessType.FIELD)
public class CompanySection extends Section{
    @Serial
    private static final long serialVersionUID = 1L;
    private List<Company> companies;


    public String getTextRepresentation() {
        String sectionType = "CompanySection";
        if (companies == null || companies.isEmpty()) {
            return sectionType + ": Нет компаний";
        }

        List<String> companyRepresentations = companies.stream()
                .map(company -> company.getNameCompany() + ", " + company.getWebsite())
                .collect(Collectors.toList());

        return sectionType + ": " + String.join(", ", companyRepresentations);
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
    public String toString() {
        return getTextRepresentation();
    }
}
