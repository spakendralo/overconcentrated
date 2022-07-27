package lu.pistache.overconcentrated;

import com.fasterxml.jackson.databind.ObjectMapper;
import lu.pistache.overconcentrated.serdes.JsonPOJOSerializer;

import java.io.IOException;
import java.io.StringWriter;

public class Serializer<T> {
    private final JsonPOJOSerializer<T> jsonPOJOSerializer = new JsonPOJOSerializer<>();

    public static String toString(Object o) {
        ObjectMapper objectMapper = new ObjectMapper();
        StringWriter stringWriter = new StringWriter();
        try {
            objectMapper.writeValue(stringWriter, o);
        } catch (IOException e) {
            return null;
        }
        return stringWriter.toString();
    }

    public String serialize(T toSerialize) {
        return new String(jsonPOJOSerializer.serialize("dumb", toSerialize));
    }
}
