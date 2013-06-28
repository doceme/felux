package com.lifecity.felux.items;

import com.google.gson.*;
import com.lifecity.felux.scenes.Scene;

import java.lang.reflect.Type;

public class ItemAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {
    private static final String CLASSNAME = "C";
    private static final String INSTANCE = "I";

    @Override
    public JsonElement serialize(T item, Type type, JsonSerializationContext context) {
        String className = item.getClass().getCanonicalName();
        JsonObject value = new JsonObject();
        value.addProperty(CLASSNAME, className);
        value.add(INSTANCE, context.serialize(item).getAsJsonObject());
        return value;
    }

    @Override
    public T deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject value = json.getAsJsonObject();
        JsonPrimitive prim = (JsonPrimitive)value.get(CLASSNAME);
        String className = prim.getAsString();

        Class<?> klass = null;

        try {
            klass = Class.forName(className);
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
            throw new JsonParseException(e.getMessage());
        }

        return context.deserialize(value.get(INSTANCE), klass);
    }
}

