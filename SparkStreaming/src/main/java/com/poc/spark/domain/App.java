package com.poc.spark.domain;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
//
//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;
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
		LOGGER.info("Iniciando  Streaming App.... INFO ");
		LOGGER.warn("Iniciando  Streaming App.... WARN ");
		LOGGER.error("Iniciando  Streaming App.... ERROR ");
		LOGGER.debug("Iniciando  Streaming App.... DEBUG ");
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
		LOGGER.info("Iniciando Obteniendo configuracion Driver .... ");

		JavaStreamingContext jssc = sparkConfigurationBuilder
				.buildJSC(sparkConfigurationBuilder.buildSparkConfiguration());
	
		Map<String, Object> kafkaParams = sparkDriverUtils.getKafkaProperties();
		Collection<String> topics = Arrays.asList(sparkDriverUtils.getTopics());// 1 o more topics

		try {
			processDStream.startRealProcess(jssc, topics, kafkaParams);
		} catch (InterruptedException e) {
			LOGGER.error("Error: InterruptedException " + e);
			throw new SparkStreamingException("InterruptedException : ", e);
		}

	}
}
