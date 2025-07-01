package com.webapp.util;

import com.google.gson.*;
import com.webapp.model.Company;
import com.webapp.model.CompanySection;
import com.webapp.model.Period;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CompanySectionAdapter implements JsonSerializer<CompanySection>, JsonDeserializer<CompanySection> {
    @Override
    public JsonElement serialize(CompanySection src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "CompanySection");

        JsonArray companiesArray = new JsonArray();
        for (Company company : src.getCompanies()) {
            JsonObject companyObject = new JsonObject();
            companyObject.addProperty("companyName", company.getNameCompany());
            companyObject.addProperty("website", company.getWebsite());

            JsonArray periodsArray = new JsonArray();
            for (Period period : company.getPeriods()) {
                JsonObject periodObject = new JsonObject();
                periodObject.addProperty("startDate", period.getStartDate().toString());
                periodObject.addProperty("endDate", period.getEndDate().toString());
                periodObject.addProperty("name", period.getName());
                periodObject.addProperty("description", period.getDescription());
                periodsArray.add(periodObject);
            }
            companyObject.add("periods", periodsArray);

            companiesArray.add(companyObject);
        }

        jsonObject.add("companies", companiesArray);
        return jsonObject;
    }

    @Override
    public CompanySection deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonArray companiesArray = jsonObject.getAsJsonArray("companies");

        List<Company> companies = new ArrayList<>();
        for (JsonElement element : companiesArray) {
            JsonObject companyObject = element.getAsJsonObject();
            String name = companyObject.get("companyName").getAsString();
            String website = companyObject.get("website").getAsString();

            List<Period> periods = new ArrayList<>();
            JsonArray periodsArray = companyObject.getAsJsonArray("periods");
            for (JsonElement periodElement : periodsArray) {
                JsonObject periodObject = periodElement.getAsJsonObject();
                LocalDate startDate = LocalDate.parse(periodObject.get("startDate").getAsString());
                LocalDate endDate = LocalDate.parse(periodObject.get("endDate").getAsString());
                String periodName = periodObject.get("name").getAsString();
                String description = periodObject.get("description").getAsString();
                periods.add(new Period(startDate, endDate, periodName, description));
            }

            companies.add(new Company(name, website, periods));
        }

        return new CompanySection(companies);
    }
}