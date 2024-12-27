package com.yebali.template

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate

class RedisTest: SpringBootTestSupport() {
    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, String>

    @Test
    fun `insert Redis`() {
        redisTemplate.opsForValue().set("key", "value")
        val value = redisTemplate.opsForValue().get("key")
        println(value)
    }
}