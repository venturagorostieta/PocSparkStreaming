package com.poc.spark.domain;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;

import com.poc.driver.spark.conf.SparkConfigurationBuilder;
import com.poc.driver.spark.util.SparkDriverUtils;
import com.poc.spark.domain.config.DomainConf;
import com.poc.spark.exception.SparkStreamingException;

@Controller
public class App {

	private static final Logger LOGGER = LogManager.getLogger(App.class);

	@Autowired
	private SparkConfigurationBuilder sparkConfigurationBuilder;

	@Autowired
	private SparkDriverUtils sparkDriverUtils;

	@Autowired
	private ProcessDStream processDStream;

	public static void main(String[] args) {
		
		Logger.getLogger("org.apache").setLevel(Level.WARN);
		LogManager.getLogger("org.apache.kafka").setLevel(Level.WARN);		
		LOGGER.warn("Iniciando  Streaming App.... WARN ");
		
		@SuppressWarnings("resource")
		AnnotationConfigApplicationContext factoria = new AnnotationConfigApplicationContext();
		factoria.register(DomainConf.class);
		factoria.refresh();

		App app = factoria.getBean(App.class);

		try {
			app.setupStreaming();
		} catch (SparkStreamingException e) {
			LOGGER.error("Error: " + e);
		}

	}

	public void setupStreaming() throws SparkStreamingException {
		LOGGER.warn("Iniciando  configuracion Driver .... ");

		JavaStreamingContext jssc = sparkConfigurationBuilder
				.buildJSC(sparkConfigurationBuilder.buildSparkConfiguration());
				
		jssc.checkpoint("C:\\tmp\\poc\\checkPoint");
		Map<String, Object> kafkaParams = sparkDriverUtils.getKafkaProperties();		
		Collection<String> topics = Arrays.asList(sparkDriverUtils.getTopics().trim().split(","));// 1 o more topics		
		LOGGER.warn("Lista de Topics: " + topics.toString());
				
		try {
			processDStream.startRealProcess(jssc, topics, kafkaParams);
		} catch (InterruptedException e) {
			LOGGER.error("Error: InterruptedException " + e);
			Thread.currentThread().interrupt();
			throw new SparkStreamingException("InterruptedException : ", e);
		}

	}
}
