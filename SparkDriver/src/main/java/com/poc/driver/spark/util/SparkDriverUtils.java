package com.poc.driver.spark.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Configuration;

import com.poc.driver.spark.constant.DriverConstants;

@Configuration
public class SparkDriverUtils {

	private String bootstrapServers;
	private String keyDeserializer;
	private String valueDeserializer;
	private String groupId;
	private String autoOffsetReset;
	private String enableAutoCommit;
	private String topics;

	private String sparkMaster;
	private String sparkAppName;
	private String sparkMaxCores;
	private String sparkExecutorMemory;
	private String sparDefaultParalelism;
	private String sparkStreamingBatchDurationSeconds;

	HashMap<String, Object> kafkaParams;
	
	
	public ResourceBundle readProperties(String fileName) {

		ResourceBundle rb = ResourceBundle.getBundle(fileName, Locale.getDefault());
		return rb;
	}

	public void readKafkaConfig() {

		ResourceBundle properties = readProperties("kafka");

		this.setBootstrapServers(properties.getString(DriverConstants.KAFKA_BOOTSTRAP_SERVER));
		this.setKeyDeserializer(properties.getString(DriverConstants.KEY_DESERIALIZER));
		this.setValueDeserializer(properties.getString(DriverConstants.VALUE_DESERIALIZER));
		this.setGroupId(properties.getString(DriverConstants.KAFKA_GROUP_ID));
		this.setAutoOffsetReset(properties.getString(DriverConstants.KAFKA_AUTO_OFFSET_RESET));
		this.setEnableAutoCommit(properties.getString(DriverConstants.KAFKA_ENABLE_AUTO_COMMIT));
		this.setTopics(properties.getString(DriverConstants.KAFKA_TOPICS));

	}

	public void readSparkConfig() {

		ResourceBundle properties = readProperties("spark");
		this.setSparkAppName(properties.getString(DriverConstants.SPARK_APP_NAME));
		this.setSparkMaster(properties.getString(DriverConstants.SPARK_MASTER));

		this.setSparkMaxCores(properties.getString(DriverConstants.SPARK_MAX_CORES));
		this.setSparkExecutorMemory(properties.getString(DriverConstants.SPARK_EXECUTOR_MEMORY));
		this.setSparDefaultParalelism(properties.getString(DriverConstants.SPARK_DEFAULT_PARALELILSM));
		this.setSparkStreamingBatchDurationSeconds(
				properties.getString(DriverConstants.SPARK_STREAMING_BATCH_DURATION_SECONDS));


	}

	public HashMap<String, Object> getKafkaProperties() {
		kafkaParams = new HashMap<>();
		readKafkaConfig();
		kafkaParams.put(DriverConstants.KAFKA_BOOTSTRAP_SERVER, getBootstrapServers());// 1 or more brokers
		kafkaParams.put(DriverConstants.KEY_DESERIALIZER, StringDeserializer.class);
		kafkaParams.put(DriverConstants.VALUE_DESERIALIZER, StringDeserializer.class);
		kafkaParams.put(DriverConstants.KAFKA_GROUP_ID, getGroupId());
		kafkaParams.put(DriverConstants.KAFKA_AUTO_OFFSET_RESET, getAutoOffsetReset());
		kafkaParams.put(DriverConstants.KAFKA_ENABLE_AUTO_COMMIT, getEnableAutoCommit());

		return kafkaParams;
	}

	@Override
	public String toString() {
		return "SparkDriverUtils [bootstrapServers=" + bootstrapServers + ", keyDeserializer=" + keyDeserializer
				+ ", valueDeserializer=" + valueDeserializer + ", groupId=" + groupId + ", autoOffsetReset="
				+ autoOffsetReset + ", enableAutoCommit=" + enableAutoCommit + ", topics=" + topics + ", sparkMaster="
				+ sparkMaster + ", sparkAppName=" + sparkAppName + ", sparkMaxCores=" + sparkMaxCores
				+ ", sparkExecutorMemory=" + sparkExecutorMemory + ", sparDefaultParalelism=" + sparDefaultParalelism
				+ ", sparkStreamingBatchDurationSeconds=" + sparkStreamingBatchDurationSeconds + ", kafkaParams="
				+ kafkaParams + "]";
	}

	public String getSparkMaxCores() {
		return sparkMaxCores;
	}

	public void setSparkMaxCores(String sparkMaxCores) {
		this.sparkMaxCores = sparkMaxCores;
	}

	public String getSparkExecutorMemory() {
		return sparkExecutorMemory;
	}

	public void setSparkExecutorMemory(String sparkExecutorMemory) {
		this.sparkExecutorMemory = sparkExecutorMemory;
	}

	public String getSparDefaultParalelism() {
		return sparDefaultParalelism;
	}

	public void setSparDefaultParalelism(String sparDefaultParalelism) {
		this.sparDefaultParalelism = sparDefaultParalelism;
	}

	public String getSparkStreamingBatchDurationSeconds() {
		return sparkStreamingBatchDurationSeconds;
	}

	public void setSparkStreamingBatchDurationSeconds(String sparkStreamingBatchDurationSeconds) {
		this.sparkStreamingBatchDurationSeconds = sparkStreamingBatchDurationSeconds;
	}

	public String getSparkMaster() {
		return sparkMaster;
	}

	public void setSparkMaster(String sparkMaster) {
		this.sparkMaster = sparkMaster;
	}

	public String getSparkAppName() {
		return sparkAppName;
	}

	public void setSparkAppName(String sparkAppName) {
		this.sparkAppName = sparkAppName;
	}

	public String getBootstrapServers() {
		return bootstrapServers;
	}

	public void setBootstrapServers(String bootstrapServers) {
		this.bootstrapServers = bootstrapServers;
	}

	public String getKeyDeserializer() {
		return keyDeserializer;
	}

	public void setKeyDeserializer(String keyDeserializer) {
		this.keyDeserializer = keyDeserializer;
	}

	public String getValueDeserializer() {
		return valueDeserializer;
	}

	public void setValueDeserializer(String valueDeserializer) {
		this.valueDeserializer = valueDeserializer;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getAutoOffsetReset() {
		return autoOffsetReset;
	}

	public void setAutoOffsetReset(String autoOffsetReset) {
		this.autoOffsetReset = autoOffsetReset;
	}

	public String getEnableAutoCommit() {
		return enableAutoCommit;
	}

	public void setEnableAutoCommit(String enableAutoCommit) {
		this.enableAutoCommit = enableAutoCommit;
	}

	public String getTopics() {
		return topics;
	}

	public void setTopics(String topics) {
		this.topics = topics;
	}

}
