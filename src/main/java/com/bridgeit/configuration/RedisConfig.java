package com.bridgeit.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

@Configuration
public class RedisConfig {
	@Bean
    JedisConnectionFactory jedisConnectionFactory() {
		
		JedisConnectionFactory jedis=new JedisConnectionFactory();
//		jedis.setHostName("localhost");
//		jedis.setPort(6379);
////		
        return new JedisConnectionFactory();
    }
 
	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		final RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		template.setConnectionFactory(jedisConnectionFactory());
		template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
		return template;
	}
}
