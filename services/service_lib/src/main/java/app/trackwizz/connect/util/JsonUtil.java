package app.trackwizz.connect.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public final class JsonUtil {

    private JsonUtil() {
    }

    /**
     * ObjectMapper Instance builder
     *
     * @return ObjectMapper
     */
    public static ObjectMapper getObjectMapper() {

        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new Jdk8Module());
        return mapper;

    }

    /**
     * converts from Json to List of given elementType
     *
     * @param json        string
     * @param elementType
     * @param <T>
     * @return List<T>
     * @throws JsonProcessingException
     */
    public static <T> List<T> toList(String json, Class<T> elementType) throws JsonProcessingException {

        final ObjectMapper mapper = getObjectMapper();
        return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(ArrayList.class, elementType));

    }

    /**
     * converts from Object to Json
     *
     * @param obj
     * @return String
     * @throws JsonProcessingException
     */
    public static String convertEntityToJson(Object obj) throws JsonProcessingException {
        final ObjectMapper mapper = getObjectMapper();
        return mapper.writeValueAsString(obj);
    }

    /**
     * Read Json and convert to given Entity type
     *
     * @param json Json String
     * @param type entity type
     * @param <T>
     * @return Entity of given entity type
     * @throws JsonProcessingException
     */
    public static <T> T convertJsonToEntity(String json, Class<T> type) throws Exception {
        try {
            final ObjectMapper mapper = getObjectMapper();
            return mapper.readValue(json, type);
        } catch (Exception exception) {
            throw new Exception("Operation Failed: Cannot convert given json into " + type.toString() + " entity");
        }

    }

    /**
     * Convert Object to given Class type
     *
     * @param object
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T parseObjectToEntity(Object object, Class<T> type) {
        final ObjectMapper mapper = getObjectMapper();
        return mapper.convertValue(object, type);
    }

    /**
     * Convert Object to List
     *
     * @param object
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T> List<T> parseObjectToList(Object object, TypeReference<List<T>> typeReference) {
        final ObjectMapper mapper = getObjectMapper();
        return mapper.convertValue(object, typeReference);
    }

    /**
     * Read Json from input-stream and return the specific node from json tree by given field name.
     *
     * @param inputStream
     * @param fieldName
     * @return
     */
    public static Object readJsonFromResource(InputStream inputStream, String fieldName) throws Exception {
        try {
            final ObjectMapper mapper = getObjectMapper();
            return mapper.readTree(inputStream).get(fieldName);
        } catch (Exception exception) {
            throw new Exception("Operation Failed: Cannot read json from given resource");
        }
    }

    /**
     * Read Json from input-stream and convert the json into given class type
     *
     * @param inputStream
     * @param type
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T readJsonFromResource(InputStream inputStream, Class<T> type) throws Exception {
        try {
            final ObjectMapper mapper = getObjectMapper();
            return mapper.readValue(inputStream, type);
        } catch (Exception exception) {
            throw new Exception("Operation Failed: Cannot read json from given resource");
        }
    }

    /**
     * Reads a list of objects from a JSON-formatted InputStream using the provided TypeReference.
     *
     * @param inputStream   The InputStream containing the JSON data.
     * @param typeReference The TypeReference specifying the object type.
     * @param <T>           The type of objects to be read.
     * @return A list of objects read from the JSON data.
     * @throws IOException If an I/O error occurs during JSON parsing.
     */
    public static <T> List<T> readJsonFromResource(InputStream inputStream, TypeReference<List<T>> typeReference) throws Exception {
        try {
            final ObjectMapper mapper = getObjectMapper();
            return mapper.readValue(inputStream, typeReference);
        } catch (Exception exception) {
            throw new Exception("Operation Failed: Cannot read json from given resource");
        }
    }

}
