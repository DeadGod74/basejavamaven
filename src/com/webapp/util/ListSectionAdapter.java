package com.webapp.util;

import com.google.gson.*;
import com.webapp.model.CompanySection;
import com.webapp.model.ListSection;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ListSectionAdapter implements JsonSerializer<ListSection>, JsonDeserializer<ListSection> {
    @Override
    public JsonElement serialize(ListSection src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "ListSection");

        JsonArray listArray = new JsonArray();
        for (String item : src.getList()) {
            listArray.add(new JsonPrimitive(item));
        }

        jsonObject.add("list", listArray);
        return jsonObject;
    }

    @Override
    public ListSection deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonArray listArray = jsonObject.getAsJsonArray("list");

        List<String> items = new ArrayList<>();
        for (JsonElement element : listArray) {
            items.add(element.getAsString());
        }

        return new ListSection(items);
    }
}
