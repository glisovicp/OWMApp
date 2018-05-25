package rs.gecko.petar.owmapp.data;

import android.content.SharedPreferences;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;

/**
 * Created by Petar on 5/24/18.
 */

public class DataCachingManager {

    private final SharedPreferences preferences;
    private final ObjectMapper objectMapper;

    public DataCachingManager(SharedPreferences preferences, ObjectMapper objectMapper) {
        this.preferences = preferences;
        this.objectMapper = objectMapper;
    }

    public <S>S get(Class<S> objectClass, String key) {
        String jsonString = preferences.getString(key, null);
        if (jsonString != null) {
            try {
                return objectMapper.readValue(jsonString, objectClass);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public <S>S get(TypeReference typeReference, String key) {
        String jsonString = preferences.getString(key, null);
        if (jsonString != null) {
            try {
                return objectMapper.readValue(jsonString, typeReference);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void save(String key, Object object) {

        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        try {
            String jsonString = objectWriter.writeValueAsString(object);
            preferences.edit().putString(key, jsonString).apply();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void clearKeyValuePair(String key) {
        preferences.edit().remove(key).commit();
    }
}
