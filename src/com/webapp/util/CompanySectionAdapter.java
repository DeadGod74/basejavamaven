package com.webapp.util;

import com.google.gson.*;
import com.webapp.model.Company;
import com.webapp.model.CompanySection;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CompanySectionAdapter implements JsonSerializer<CompanySection>, JsonDeserializer<CompanySection>{
    @Override
    public JsonElement serialize(CompanySection src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "CompanySection");

        JsonArray companiesArray = new JsonArray();
        for (Company company : src.getCompanies()) {
            JsonObject companyObject = new JsonObject();
            companyObject.addProperty("name", company.getNameCompany());
            companyObject.addProperty("website", company.getWebsite());
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
            String name = companyObject.get("name").getAsString();
            String website = companyObject.get("website").getAsString();
            companies.add(new Company(name, website, new ArrayList<>()));
        }

        return new CompanySection(companies);
    }
}
