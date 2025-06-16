package com.webapp.util;

import com.google.gson.*;
import com.webapp.model.CompanySection;
import com.webapp.model.ListSection;
import com.webapp.model.Section;
import com.webapp.model.TextSection;

import java.lang.reflect.Type;

public class SectionAdapter implements JsonSerializer<Section>, JsonDeserializer<Section> {
    @Override
    public JsonElement serialize(Section src, Type typeOfSrc, JsonSerializationContext context) {
        if (src instanceof CompanySection) {
            return new CompanySectionAdapter().serialize((CompanySection) src, typeOfSrc, context);
        } else if (src instanceof TextSection) {
            return new TextSectionAdapter().serialize((TextSection) src, typeOfSrc, context);
        } else if (src instanceof ListSection) {
            return new ListSectionAdapter().serialize((ListSection) src, typeOfSrc, context);
        }
        throw new JsonParseException("Unknown section type: " + src.getClass());
    }

    @Override
    public Section deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();

        if ("CompanySection".equals(type)) {
            return new CompanySectionAdapter().deserialize(json, typeOfT, context);
        } else if ("TextSection".equals(type)) {
            return new TextSectionAdapter().deserialize(json, typeOfT, context);
        } else if ("ListSection".equals(type)) {
            return new ListSectionAdapter().deserialize(json, typeOfT, context);
        }
        throw new JsonParseException("Unknown section type: " + type);
    }
}
