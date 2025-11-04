package org.example.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RedisCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public RedisCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;

        // Initialize ObjectMapper for Java 8 Date/Time types
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
    }

    /* ------------------- Key Operations ------------------- */
    public boolean exists(String key) {
        Boolean result = redisTemplate.hasKey(key);
        return result != null && result;
    }

    public boolean delete(String key) {
        Boolean result = redisTemplate.delete(key);
        return result != null && result;
    }

    public void expire(String key, long timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }

    public void expire(String key, Duration duration) {
        redisTemplate.expire(key, duration);
    }

    /* ------------------- Value Operations ------------------- */
    public void setValue(String key, Object value) {
        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, json);
            log.debug("Set value for key: {}", key);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize value for key [{}]", key, e);
        }
    }

    public void setValueWithTime(String key, Object value, long timeout, TimeUnit unit) {
        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, json, timeout, unit);
            log.debug("Set value with TTL for key: {}", key);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize value for key [{}]", key, e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(String key, Class<T> clazz) {
        Object raw = redisTemplate.opsForValue().get(key);
        if (raw == null) return null;

        if (clazz.isInstance(raw)) return (T) raw;

        try {
            return objectMapper.readValue(raw.toString(), clazz);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize cache value for key [{}]", key, e);
            return null;
        }
    }

    /* ------------------- Hash Operations ------------------- */
    public void putHash(String key, String hashKey, Object value) {
        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForHash().put(key, hashKey, json);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize hash value for key [{}], hashKey [{}]", key, hashKey, e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getHashValue(String key, String hashKey, Class<T> clazz) {
        Object raw = redisTemplate.opsForHash().get(key, hashKey);
        if (raw == null) return null;

        if (clazz.isInstance(raw)) return (T) raw;

        try {
            return objectMapper.readValue(raw.toString(), clazz);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize hash value for key [{}], hashKey [{}]", key, hashKey, e);
            return null;
        }
    }

    public Map<Object, Object> getHashEntries(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public void deleteHashKey(String key, String hashKey) {
        redisTemplate.opsForHash().delete(key, hashKey);
    }

    /* ------------------- List Operations ------------------- */
    public void pushToList(String key, Object value) {
        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForList().rightPush(key, json);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize list value for key [{}]", key, e);
        }
    }

    public List<Object> getListRange(String key, long start, long end) {
        List<Object> rawList = redisTemplate.opsForList().range(key, start, end);
        if (rawList == null) return Collections.emptyList();

        return rawList.stream().map(v -> {
            try {
                return objectMapper.readValue(v.toString(), Object.class);
            } catch (JsonProcessingException e) {
                log.error("Failed to deserialize list element for key [{}]", key, e);
                return null;
            }
        }).filter(Objects::nonNull).toList();
    }

    public Object popFromList(String key) {
        Object raw = redisTemplate.opsForList().leftPop(key);
        if (raw == null) return null;

        try {
            return objectMapper.readValue(raw.toString(), Object.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize list element for key [{}]", key, e);
            return null;
        }
    }

    /* ------------------- Set Operations ------------------- */
    public void addToSet(String key, Object... values) {
        for (Object v : values) {
            try {
                String json = objectMapper.writeValueAsString(v);
                redisTemplate.opsForSet().add(key, json);
            } catch (JsonProcessingException e) {
                log.error("Failed to serialize set value for key [{}]", key, e);
            }
        }
    }

    public Set<Object> getSetMembers(String key) {
        Set<Object> rawSet = redisTemplate.opsForSet().members(key);
        if (rawSet == null) return Collections.emptySet();

        return rawSet.stream().map(v -> {
            try {
                return objectMapper.readValue(v.toString(), Object.class);
            } catch (JsonProcessingException e) {
                log.error("Failed to deserialize set element for key [{}]", key, e);
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    public boolean isMemberOfSet(String key, Object value) {
        try {
            String json = objectMapper.writeValueAsString(value);
            Boolean result = redisTemplate.opsForSet().isMember(key, json);
            return result != null && result;
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize set membership check for key [{}]", key, e);
            return false;
        }
    }

    /* ------------------- Counter / Increment ------------------- */
    public Long increment(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    public Long incrementBy(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    public Long decrement(String key) {
        return redisTemplate.opsForValue().decrement(key);
    }

    /* ------------------- Alias-based caching ------------------- */
    public void storeWithAliases(String namespace, String canonicalId, Object value, Map<String, String> aliases, Duration ttl) {
        String canonicalKey = namespace + ":uuid:" + canonicalId;

        setValueWithTime(canonicalKey, value, ttl.toMinutes(), TimeUnit.MINUTES);

        if (aliases != null) {
            aliases.forEach((aliasType, aliasValue) -> {
                String aliasKey = namespace + ":" + aliasType + ":" + aliasValue;
                setValueWithTime(aliasKey, canonicalKey, ttl.toMinutes(), TimeUnit.MINUTES);
            });
        }

        log.debug("Stored {} with canonical key [{}] and aliases {}", namespace, canonicalKey, aliases);
    }

    public <T> T getWithAliases(String namespace, String keyType, String keyValue, Class<T> clazz) {
        String key = namespace + ":" + keyType + ":" + keyValue;

        Object raw = redisTemplate.opsForValue().get(key);
        if (raw == null) return null;

        // If this is an alias, resolve canonical
        if (raw instanceof String && ((String) raw).startsWith(namespace)) {
            raw = redisTemplate.opsForValue().get((String) raw);
        }

        if (clazz.isInstance(raw)) return clazz.cast(raw);

        try {
            String json = objectMapper.writeValueAsString(raw);
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize cache value for key [{}]", key, e);
            return null;
        }
    }

    public void evictWithAliases(String namespace, String canonicalId, Map<String, String> aliases) {
        String canonicalKey = namespace + ":uuid:" + canonicalId;
        delete(canonicalKey);

        if (aliases != null) {
            aliases.forEach((aliasType, aliasValue) -> {
                String aliasKey = namespace + ":" + aliasType + ":" + aliasValue;
                delete(aliasKey);
            });
        }

        log.debug("Evicted cache for canonical [{}] and aliases {}", canonicalKey, aliases);
    }
}


