package com.webapp.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.webapp.util.LocalDateGsonAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serial;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@XmlAccessorType(XmlAccessType.FIELD)
public class CompanySection extends Section{
    @Serial
    private static final long serialVersionUID = 1L;
    private List<Company> companies;
    private String type;


    public String getTextRepresentation() {
        String sectionType = "CompanySection";
        if (companies == null || companies.isEmpty()) {
            return sectionType + ": Нет компаний";
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateGsonAdapter())
                .create();

        List<CompanyRepresentation> companyRepresentations = companies.stream()
                .map(company -> {
                    CompanyRepresentation representation = new CompanyRepresentation();
                    representation.setName(company.getNameCompany());
                    representation.setWebsite(company.getWebsite());
                    representation.setPeriods(company.getPeriods());

                    return representation;
                })
                .collect(Collectors.toList());

        String jsonOutput = gson.toJson(companyRepresentations);

        return sectionType + ": " + jsonOutput;
    }
    public CompanySection() {
        this.type = "CompanySection";
    }

    public CompanySection(List<Company> companies) {
        this.type = "CompanySection";
        this.companies = companies;
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

    class CompanyRepresentation {
        private String name;
        private String website;
        private List<Period> periods;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public List<Period> getPeriods() {
            return periods;
        }

        public void setPeriods(List<Period> periods) {
            this.periods = periods;
        }
    }

}
