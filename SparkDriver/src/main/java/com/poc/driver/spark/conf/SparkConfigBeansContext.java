package com.poc.driver.spark.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.poc.driver.spark.util.SparkDriverUtils;

@Configuration
public class SparkConfigBeansContext {
	
	@Bean(name="sparkUtils")
	public SparkDriverUtils getDriverUtils(){
		return new SparkDriverUtils();
	}
	
	@Bean(name="sparkConfigurationBuilder")
	public SparkConfigurationBuilder getSparkConfigurationBuilder(){
		return new SparkConfigurationBuilder();
	}

}
