/*
 * *
 *  * Copyright 2012 fenbi.com. All rights reserved.
 *  * FENBI.COM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package fun.sure.any.common.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author wanghb
 */
public class GsonHelper {

    private static final String TAG = GsonHelper.class.getSimpleName();

    private static final GsonBuilder DESERIALIZER_BUILDER = new GsonBuilder();
    private static Gson deserializer;

    private static final GsonBuilder SERIALIZER_BUILDER = new GsonBuilder();
    private static Gson serializer;

    public static Gson getSerializer() {
        if (serializer != null) {
            return serializer;
        }
        synchronized (GsonHelper.class) {
            if (serializer == null) {
                serializer = SERIALIZER_BUILDER.create();
            }
        }
        return serializer;
    }

    public static Gson getDeserializer() {
        if (deserializer != null) {
            return deserializer;
        }
        synchronized (GsonHelper.class) {
            if (deserializer == null) {
                deserializer = DESERIALIZER_BUILDER.create();
            }
        }
        return deserializer;
    }

    public static <T> void registerDeserializer(Class<T> clazz, JsonDeserializer<T> d) {
        synchronized (GsonHelper.class) {
            DESERIALIZER_BUILDER.registerTypeAdapter(clazz, d);
            deserializer = DESERIALIZER_BUILDER.create();
        }
    }

    public static String getString(JsonElement jsonElement, String key,
            String defValue) {
        return getString(jsonElement.getAsJsonObject(), key, defValue);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        T instance;
        try {
            instance = getDeserializer().fromJson(json, typeOfT);
        } catch (JsonSyntaxException e) {
            Log.e(TAG, e.toString());
            instance = null;
        }
        return instance;
    }

    public static <T> T fromJson(JsonElement element, Type typeOfT) {
        T instance;
        try {
            instance = getDeserializer().fromJson(element, typeOfT);
        } catch (JsonSyntaxException e) {
            Log.e(TAG, e.toString());
            instance = null;
        }
        return instance;
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        T instance;
        try {
            instance = getDeserializer().fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            Log.e(TAG, e.toString());
            instance = null;
        }
        return instance;
    }

    public static <T> T fromJson(JsonElement element, Class<T> clazz) {
        T instance;
        try {
            instance = getDeserializer().fromJson(element, clazz);
        } catch (JsonSyntaxException e) {
            instance = null;
            Log.e(TAG, e.toString());
        }
        return instance;
    }

    /**
     * 使用 {@link ResponseHelper#parseList(JsonElement, Type)}
     */
    @Deprecated
    public static <T> List<T> parseList(JsonElement ele, Type type) {
        List<T> list;
        try {
            list = getDeserializer().fromJson(ele, type);
        } catch (JsonSyntaxException e) {
            Log.e(TAG, e.toString());
            list = null;
        }
        return list;
    }

    /**
     * 使用 {@link ResponseHelper#parseList(JsonElement, Type)}
     */
    public static <T> List<T> parseList(String str, Type type) {
        List<T> list;
        try {
            list = getDeserializer().fromJson(str, type);
        } catch (JsonSyntaxException e) {
            Log.e(TAG, e.toString());
            list = null;
        }
        return list;
    }

    public static <T> T jsonToArray(JsonElement ele, Class<T> arrayClazz) {
        try {
            return getDeserializer().fromJson(ele, arrayClazz);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    public static String toJson(Object object) {
        return getSerializer().toJson(object);
    }

    public static int getSafeJsonInt(JsonElement element, int defaultValue) {
        if (element == null || !element.isJsonPrimitive()) {
            return defaultValue;
        }
        JsonPrimitive primitive = element.getAsJsonPrimitive();
        try {
            return primitive.getAsInt();
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
