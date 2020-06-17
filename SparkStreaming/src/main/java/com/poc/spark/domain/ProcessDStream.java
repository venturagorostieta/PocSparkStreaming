package com.poc.spark.domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import scala.Tuple2;

@Controller
public class ProcessDStream  implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LogManager.getLogger(ProcessDStream.class);
	
	@Autowired
	private CustomersRDD customersRDD;

	public void startRealProcess(JavaStreamingContext jssc, Collection<String> topics, Map<String, Object> kafkaParams)
			throws InterruptedException {

		JavaInputDStream<ConsumerRecord<String, String>> stream = KafkaUtils.createDirectStream(jssc,
				LocationStrategies.PreferConsistent(),
				ConsumerStrategies.<String, String>Subscribe(topics, kafkaParams));
		
		//stream.filter( f -> f.topic().equals("customers-topic"));
		
		JavaDStream<String> customers = stream.map(kafkaRecord -> kafkaRecord.value()).filter(f-> f.contains("customers"));
		
		//-------------------------------------------------------------------------------
		
		customersRDD.startCustomersProccesor(customers);
		
		
		// ------------------------------------------------------------------------------
		
		JavaDStream<String> words = customers.flatMap(line -> Arrays.asList(line.split(" ")).iterator());

		JavaPairDStream<String, Integer> wordMap = words.mapToPair(word -> new Tuple2<>(word, 1));

		JavaPairDStream<String, Integer> wordCount = wordMap.reduceByKey((first, second) -> first + second);

		wordCount.print();
		
		wordCount.foreachRDD(rdd -> rdd.foreach(x -> LOGGER.warn(x)));
		

		jssc.start();
		jssc.awaitTermination();

	}

}
