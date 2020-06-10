package com.poc.driver.spark.constant;

import java.util.regex.Pattern;

public final class DriverConstants {
	
	public static final String  EJEMPLO ="Texto desde driver";
	 public static final String SPARK_MASTER = "spark.setMaster";
	 public static final String SPARK_APP_NAME = "spark.appName";
	 public static final String SPARK_MAX_CORES = "spark.cores.max";
	 public static final String SPARK_EXECUTOR_MEMORY = "spark.executor.memory";
	 public static final String SPARK_DEFAULT_PARALELILSM = "spark.default.parallelism";
	 public static final String SPARK_STREAMING_BATCH_DURATION_SECONDS = "spark.streaming.batch.duration.seconds";
	
	 
	 public static final String KAFKA_BOOTSTRAP_SERVER = "bootstrap.servers";
	 public static final String KEY_DESERIALIZER = "key.deserializer";
	 public static final String VALUE_DESERIALIZER = "value.deserializer";
	 public static final String KAFKA_GROUP_ID = "group.id";
	 public static final String KAFKA_AUTO_OFFSET_RESET = "auto.offset.reset";
	 public static final String KAFKA_ENABLE_AUTO_COMMIT = "enable.auto.commit";
	 public static final String KAFKA_TOPICS = "topics";
	 
	 public static final Pattern SPACE = Pattern.compile(" ");

}
