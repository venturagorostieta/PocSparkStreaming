package com.poc.driver.spark.conf;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.poc.driver.spark.constant.DriverConstants;
import com.poc.driver.spark.util.SparkDriverUtils;

@Service
@Configurable
public class SparkConfigurationBuilder {

	@Autowired
	@Qualifier("sparkUtils")
	private SparkDriverUtils sparkDriverUtils;
	

	public SparkConf buildSparkConfiguration() {
		sparkDriverUtils.readSparkConfig();

		return new SparkConf().setMaster(sparkDriverUtils.getSparkMaster())
				.setAppName(sparkDriverUtils.getSparkAppName() == null ? "name-not-set"
						: sparkDriverUtils.getSparkAppName())
				.set("spark.default.parallelism", "2")
				//for prod
				.set("spark.yarn.maxAppAttempts", "1")
				.set("spark.yarn.am.attemptFailuresValidityInterval", "2h")
				.set("spark.streaming.receiver.writeAheadLog.enable", "true")
				.set("spark.streaming.stopGracefullyOnShutdown" , "true")
				 			
				.set(DriverConstants.SPARK_MAX_CORES, sparkDriverUtils.getSparkMaxCores())
				.set(DriverConstants.SPARK_EXECUTOR_MEMORY, sparkDriverUtils.getSparkExecutorMemory());	
	}

	public JavaStreamingContext buildJSC(SparkConf sparkConf) {

		Long seconds = Long.valueOf(sparkDriverUtils.getSparkStreamingBatchDurationSeconds());		
		return new JavaStreamingContext(buildSparkConfiguration(), Durations.seconds(seconds));
	}
	
	public SparkDriverUtils getSparkDriverUtils() {
		return sparkDriverUtils;
	}

	public void setSparkDriverUtils(SparkDriverUtils sparkDriverUtils) {
		this.sparkDriverUtils = sparkDriverUtils;
	}

}
