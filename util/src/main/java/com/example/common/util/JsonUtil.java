package com.example.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @Author xiapeng
 * @Date: 2022/01/31/12:45 上午
 * @Description:json工具类
 */
@Slf4j
@Data
public class JsonUtil {
    /**
     * 单例
     */
    public static final JsonUtil INSTANCE = new JsonUtil();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String CLASS_NULL = "class is null";

    private JsonUtil() {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);
        objectMapper.getFactory().disable(JsonFactory.Feature.INTERN_FIELD_NAMES);
    }

    /**
     * jsonNode转object
     *
     * @param jsonNode jsonNode
     * @param clazz    类型
     * @param <T>      通配符
     * @return object
     */
    public static <T> T toObject(JsonNode jsonNode, Class<T> clazz) {
        return INSTANCE.asObject(jsonNode, clazz);
    }

    /**
     * json转object
     *
     * @param json  json字符串
     * @param clazz 被转化类型
     * @param <T>   通配符
     * @return object
     */
    public static <T> T toObject(String json, Class<T> clazz) {
        return INSTANCE.asObject(json, clazz);
    }

    /**
     * json转object
     *
     * @param json         json字符串
     * @param wrapperClass 包装类对象类型
     * @param typeClass    类型类对象类型
     * @param <T>          包装类通配符
     * @param <W>          类型类通配符
     * @return object
     */
    public static <T, W> W toObject(String json, Class<W> wrapperClass, Class<T> typeClass) {
        return INSTANCE.asObject(json, wrapperClass, typeClass);
    }

    /**
     * 通过TypeReference和json转对象
     *
     * @param json          json字符串
     * @param typeReference 类型
     * @param <T>通配符
     * @return object
     */
    public static <T> T toObject(String json, TypeReference<T> typeReference) {
        return INSTANCE.asObject(json, typeReference);
    }

    /**
     * bytes转对象
     *
     * @param json  json二进制数组
     * @param clazz 被转对象的类型
     * @param <T>   通配符
     * @return object
     */
    public static <T> T toObject(byte[] json, Class<T> clazz) {
        return INSTANCE.asObject(json, clazz);
    }

    /**
     * json转list
     *
     * @param json  json字符串
     * @param clazz 被转对象的类型
     * @param <T>   通配符
     * @return 列表
     */
    public static <T> List<T> toList(String json, Class<T> clazz) {
        return INSTANCE.asList(json, clazz);
    }

    /**
     * json转set
     *
     * @param json  json字符串
     * @param clazz 被转对象的类型
     * @param <T>   通配符
     * @return set
     */
    public static <T> Set<T> toSet(String json, Class<T> clazz) {
        return INSTANCE.asSet(json, clazz);
    }

    /**
     * json转map
     *
     * @param json       json字符串
     * @param keyClass   key类型
     * @param valueClass value类型
     * @param <K>        键类型
     * @param <V>        值类型
     * @return map
     */
    public static <K, V> Map<K, V> toMap(String json, Class<K> keyClass, Class<V> valueClass) {
        return INSTANCE.asMap(json, keyClass, valueClass);
    }

    /**
     * 对象转json
     *
     * @param object 被转化的对象
     * @return 对象的json字符串
     */
    public static String toString(Object object) {
        return INSTANCE.asString(object);
    }

    /**
     * 对象转String
     *
     * @param object 对象
     * @param pretty 是否带缩进
     * @return string
     */
    public static String toString(Object object, boolean pretty) {
        return INSTANCE.asString(object, pretty);
    }

    /**
     * clone新对象
     *
     * @param object 被克隆对象
     * @param clazz  目标类型
     * @param <T>    类型参数
     * @return新对象
     */
    public static <T> T clone(T object, Class<T> clazz) {
        return INSTANCE.asClone(object, clazz);
    }

    /**
     * 对象转ObjectNode
     *
     * @param object 对象
     * @return ObjectNode
     */
    public static ObjectNode getNode(Object object) {
        return INSTANCE.asNode(object);
    }

    /**
     * json转object
     *
     * @param json  json字符串
     * @param clazz 被转化类型
     * @param <T>   通配符
     * @return object
     */
    public <T> T asObject(String json, Class<T> clazz) {
        try {
            if (StringUtils.isBlank(json)) {
                return null;
            }
            ValidateUtil.notNull(clazz, CLASS_NULL);
            if (clazz == String.class) {
                return (T) json;
            }
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new JsonException(e);
        }
    }

    /**
     * jsonNode转object
     *
     * @param jsonNode jsonNode
     * @param clazz    类型
     * @param <T>      通配符
     * @return object
     */
    public <T> T asObject(JsonNode jsonNode, Class<T> clazz) {
        if (jsonNode == null) {
            return null;
        }
        try {
            ValidateUtil.notNull(clazz, CLASS_NULL);
            return objectMapper.treeToValue(jsonNode, clazz);
        } catch (Exception e) {
            throw new JsonException(e);
        }
    }

    /**
     * json转object
     *
     * @param json         json字符串
     * @param wrapperClass 包装类对象类型
     * @param typeClass    类型类对象类型
     * @param <T>          包装类通配符
     * @param <W>          类型类通配符
     * @return object
     */
    public <T, W> W asObject(String json, Class<W> wrapperClass, Class<T> typeClass) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            ValidateUtil.notNull(wrapperClass, "wrapperClass is null");
            ValidateUtil.notNull(typeClass, "typeClass is null");
            JavaType type = objectMapper.getTypeFactory().constructParametricType(wrapperClass, typeClass);
            return objectMapper.readValue(json, type);
        } catch (Exception e) {
            throw new JsonException(e);
        }
    }

    /**
     * 通过TypeReference和json转对象
     *
     * @param json          json字符串
     * @param typeReference 类型
     * @param <T>通配符
     * @return object
     */
    public <T> T asObject(String json, TypeReference<T> typeReference) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            ValidateUtil.notNull(typeReference, "typeReference is null");
            return objectMapper.readValue(json, typeReference);
        } catch (Exception e) {
            throw new JsonException(e);
        }
    }

    /**
     * bytes转对象
     *
     * @param bytes json二进制数组
     * @param clazz 被转对象的类型
     * @param <T>   通配符
     * @return object
     */
    public <T> T asObject(byte[] bytes, Class<T> clazz) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return asObject(new String(bytes, StandardCharsets.UTF_8), clazz);
    }

    /**
     * json转list
     *
     * @param json  json字符串
     * @param clazz 被转对象的类型
     * @param <T>   通配符
     * @return 列表
     */
    public <T> List<T> asList(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json)) {
            return new ArrayList<>(0);
        }
        try {
            ValidateUtil.notNull(clazz, CLASS_NULL);
            JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
            return objectMapper.readValue(json, type);
        } catch (Exception e) {
            throw new JsonException(e);
        }
    }

    /**
     * json转set
     *
     * @param json  json字符串
     * @param clazz 被转对象的类型
     * @param <T>   通配符
     * @return set
     */
    public <T> Set<T> asSet(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json)) {
            return new HashSet<>(0);
        }
        try {
            ValidateUtil.notNull(clazz, CLASS_NULL);
            JavaType type = objectMapper.getTypeFactory().constructCollectionType(Set.class, clazz);
            return objectMapper.readValue(json, type);
        } catch (Exception e) {
            throw new JsonException(e);
        }
    }

    /**
     * json转map
     *
     * @param json       json字符串
     * @param keyClass   key类型
     * @param valueClass value类型
     * @param <K>        键类型
     * @param <V>        值类型
     * @return map
     */
    public <K, V> Map<K, V> asMap(String json, Class<K> keyClass, Class<V> valueClass) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            ValidateUtil.notNull(keyClass, "key class is null");
            ValidateUtil.notNull(valueClass, "value class is null");
            JavaType type = objectMapper.getTypeFactory().constructParametricType(Map.class, keyClass, valueClass);
            return objectMapper.readValue(json, type);
        } catch (Exception e) {
            throw new JsonException(e);
        }
    }

    /**
     * json转jsonNode
     *
     * @param json json字符串
     * @return jsonnode
     */
    public static JsonNode readTree(String json) {
        return INSTANCE.asTree(json);
    }

    /**
     * 对象转String
     *
     * @param object 对象
     * @param pretty 是否带缩进
     * @return string
     */
    public String asString(Object object, boolean pretty) {
        return pretty ? prettyAsString(object) : asString(object);
    }

    /**
     * 对象转json
     *
     * @param object 被转化的对象
     * @return 对象的json字符串
     */
    public String asString(Object object) {
        if (object == null) {
            return "";
        }
        if (object instanceof String) {
            return (String) object;
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JsonException(e);
        }
    }

    /**
     * 对象转带缩进的json
     *
     * @param object 被转化的对象
     * @return 对象的字符串
     */
    public String prettyAsString(Object object) {
        if (object == null) {
            return "";
        }
        try {
            if (object instanceof String) {
                String string = (String) object;
                if (!string.startsWith("{") || !string.endsWith("}")) {
                    return string;
                }
                JsonNode jsonNode = readTree(string);
                return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
            }
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (Exception e) {
            throw new JsonException(e);
        }
    }

    /**
     * json转jsonNode
     *
     * @param json json字符串
     * @return jsonnode
     */
    public JsonNode asTree(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return objectMapper.readTree(json);
        } catch (Exception e) {
            throw new JsonException(e);
        }
    }

    /**
     * clone新对象
     *
     * @param object 被克隆对象
     * @param clazz  目标类型
     * @param <T>    类型参数
     * @return新对象
     */
    public <T> T asClone(T object, Class<T> clazz) {
        if (object == null) {
            return null;
        }
        try {
            ValidateUtil.notNull(clazz, CLASS_NULL);
            String json = toString(object);
            return toObject(json, clazz);
        } catch (Exception e) {
            throw new JsonException(e);
        }
    }

    /**
     * 对象转ObjectNode
     *
     * @param object 对象
     * @return ObjectNode
     */
    public ObjectNode asNode(Object object) {
        if (object == null) {
            return null;
        }
        return objectMapper.convertValue(object, ObjectNode.class);
    }

    /**
     * json序列化/反序列化发生的异常
     */
    public static class JsonException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        private JsonException(Throwable cause) {
            super(cause);
        }
    }
}
