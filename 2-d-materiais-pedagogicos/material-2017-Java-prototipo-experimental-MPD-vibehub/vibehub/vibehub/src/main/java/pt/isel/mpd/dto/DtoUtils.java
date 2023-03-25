package pt.isel.mpd.dto;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Array;
import java.util.function.Function;

public class DtoUtils {

    public static int intFromJson(JsonObject obj, String field){
        if(obj.has(field) && !obj.get(field).isJsonNull())
            return obj.get(field).getAsInt();
        return 0;
    }

    public static String stringFromJson(JsonObject obj, String field){
        if(obj.has(field) && !obj.get(field).isJsonNull())
            return obj.get(field).getAsString();
        return "";
    }

    public static <R> R objectFromJson(JsonObject obj, String field, Function<JsonObject, R> f){
        if(obj.has(field) && !obj.get(field).isJsonPrimitive())
            return f.apply(obj.get(field).getAsJsonObject());
        return f.apply(null);
    }

    public static <R> R[] arrayFromJson(JsonObject obj, String field, Function<JsonObject, R> f, Class<R> c){
        if(!obj.has(field) || obj.get(field).isJsonPrimitive())
            return (R[]) Array.newInstance(c, 0);
        R[] result;
        JsonElement element = obj.get(field);
        if(element.isJsonArray()) {
            JsonArray array = obj.get(field).getAsJsonArray();
            result = (R[]) Array.newInstance(c, array.size());
            for (int i = 0; i < array.size(); i++)
                result[i] = f.apply(array.get(i).getAsJsonObject());
        } else {
            result = (R[]) Array.newInstance(c, 1);
            result[0] = f.apply(element.getAsJsonObject());
        }
        return result;
    }
}
