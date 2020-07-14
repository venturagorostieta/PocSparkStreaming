package com.poc.spark.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ProcessDStream  implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LogManager.getLogger(ProcessDStream.class);
	
	@Autowired
	private CustomersRDD customersRDD;
	
	@Autowired
	private TransactionsRDD transactionsRDD;

	public void startRealProcess(JavaStreamingContext jssc, Collection<String> topics, Map<String, Object> kafkaParams)
			 {

		JavaInputDStream<ConsumerRecord<String, String>> stream = KafkaUtils.createDirectStream(jssc,
				LocationStrategies.PreferConsistent(),
				ConsumerStrategies.<String, String>Subscribe(topics, kafkaParams));
		
		//customers-struct-topic , "transactions-struct-topic
		//mysql-demo-transactions,mysql-demo-customers
		JavaDStream<String> customers =  stream.filter( f -> f.topic().equals("customers-struct-topic")).map(kafkaRecord -> kafkaRecord.value());		
		JavaDStream<String> transactions =  stream.filter( f -> f.topic().equals("transactions-struct-topic")).map(kafkaRecord -> kafkaRecord.value());
		
		LOGGER.warn("Enviando RDDs ...");
		transactionsRDD.startTransactionsProccesor(transactions);		
		customersRDD.startCustomersProccesor(customers);

		transactions.print();		

	}

}
