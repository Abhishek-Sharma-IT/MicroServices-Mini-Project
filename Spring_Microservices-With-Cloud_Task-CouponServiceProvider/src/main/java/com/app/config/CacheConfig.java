package com.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.MaxSizeConfig.MaxSizePolicy;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;

@Configuration
public class CacheConfig {

	@Bean
	public Config cacheConfig() {
		
		return new Config()      // Creating Cache
				.setInstanceName("hazelcast-cache")  // Setting Cache Name
				.addMapConfig(            // Setting MapConfig Module
						new MapConfig()       
						   .setName("coupon-cache")  // MapConfig Name
						   .setTimeToLiveSeconds(30000)  // Max Time for Object in Memory if no Activity Done
						   .setMaxSizeConfig(new MaxSizeConfig(200,MaxSizePolicy.FREE_HEAP_SIZE))  // Size of Object in MapConfig
						   .setEvictionPolicy(EvictionPolicy.LRU)
						);
	}
}
