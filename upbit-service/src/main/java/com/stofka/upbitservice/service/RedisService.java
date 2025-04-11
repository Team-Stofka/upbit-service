package com.stofka.upbitservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public <T> List<T> getCandlesFromRedis(String redisKey, Class<T> clazz, int expectedSize) {
        List<String> jsonList = redisTemplate.opsForList().range(redisKey, 0, expectedSize - 1);

//        디버깅 코드
//        System.out.println("[REDIS 조회] 키: " + redisKey + ", 결과 개수: " + (jsonList != null ? jsonList.size() : 0));

        if (jsonList == null || jsonList.size() < expectedSize) {
//            디버깅 코드
//            System.out.println("[REDIS MISS] 캐시된 데이터 부족 또는 없음. DB 조회 필요.");
            return null;
        }

        return jsonList.stream().map(json -> {
            try {
                return objectMapper.readValue(json, clazz);
            } catch (IOException e) {
                throw new RuntimeException("JSON 파싱 오류", e);
            }
        }).toList();
    }

    public <T> void saveCandlesToRedis(String redisKey, List<T> candles, Duration ttl) {
        List<String> jsonList = candles.stream().map(candle -> {
            try {
                return objectMapper.writeValueAsString(candle);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("JSON 직렬화 오류", e);
            }
        }).toList();

        // 기존 데이터 삭제 후 재저장
        redisTemplate.delete(redisKey);
        redisTemplate.opsForList().rightPushAll(redisKey, jsonList);
        redisTemplate.expire(redisKey, ttl);
    }

}