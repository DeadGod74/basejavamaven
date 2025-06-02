package com.webapp.util;

import com.google.gson.*;
import com.webapp.model.TextSection;

import java.lang.reflect.Type;

public class TextSectionAdapter implements JsonSerializer<TextSection>, JsonDeserializer<TextSection> {
    @Override
    public JsonElement serialize(TextSection src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getText()); // Сериализуем как строку
    }

    @Override
    public TextSection deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new TextSection(json.getAsString()); // Десериализуем из строки
    }
}