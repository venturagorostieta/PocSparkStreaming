package com.poc.driver.spark.conf;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import com.poc.driver.spark.constant.DriverConstants;
import com.poc.driver.spark.util.SparkDriverUtils;

@Service
@Configurable
public class SparkConfigurationBuilder {

	@Autowired
	@Qualifier("sparkUtils")
	private SparkDriverUtils sparkDriverUtils;
	
	private static ApplicationContext context;

	public SparkConf buildSparkConfiguration() {
		sparkDriverUtils.readSparkConfig();

		SparkConf sparkConf = new SparkConf().setMaster(sparkDriverUtils.getSparkMaster())
				.setAppName(sparkDriverUtils.getSparkAppName() == null ? "name-not-set"
						: sparkDriverUtils.getSparkAppName())
				.set(DriverConstants.SPARK_MAX_CORES, sparkDriverUtils.getSparkMaxCores())
				.set(DriverConstants.SPARK_EXECUTOR_MEMORY, sparkDriverUtils.getSparkExecutorMemory())
				.set(DriverConstants.SPARK_DEFAULT_PARALELILSM, sparkDriverUtils.getSparDefaultParalelism());
		
		return sparkConf;
	}

	public JavaStreamingContext buildJSC(SparkConf sparkConf) {

		Long seconds = Long.valueOf(sparkDriverUtils.getSparkStreamingBatchDurationSeconds());

		return new JavaStreamingContext(buildSparkConfiguration(), Durations.seconds(seconds));

	}
	
	public static void main (String args []) {
		
		context = new AnnotationConfigApplicationContext(SparkConfigBeansContext.class);
		SparkConfigurationBuilder helloWorldBean = context.getBean("sparkConfigurationBuilder", SparkConfigurationBuilder.class);
		helloWorldBean.buildJSC(helloWorldBean.buildSparkConfiguration());
		
	}

	public SparkDriverUtils getSparkDriverUtils() {
		return sparkDriverUtils;
	}

	public void setSparkDriverUtils(SparkDriverUtils sparkDriverUtils) {
		this.sparkDriverUtils = sparkDriverUtils;
	}
	

}
