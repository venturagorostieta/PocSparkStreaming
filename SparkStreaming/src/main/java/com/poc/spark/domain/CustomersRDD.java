package com.poc.spark.domain;

import java.io.IOException;
import java.io.Serializable;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.poc.spark.model.Customers;
import com.poc.spark.util.JsonMapperUtil;

@Controller
public class CustomersRDD implements Serializable {
	private static final Logger LOGGER = LogManager.getLogger(CustomersRDD.class);

	private static final long serialVersionUID = 1L;
	@Autowired
	private JsonMapperUtil jsonMapperUtil;

	public void startCustomersProccesor(JavaDStream<String> customers) {

		customers.foreachRDD(rdd -> rdd.foreach(x -> {
			LOGGER.info(x);
			filteGenderCustomers(x);

		}));
	}

	private void filteGenderCustomers(String row) throws JsonParseException, JsonMappingException, IOException {

		Customers cust = jsonMapperUtil.rowToModel(row);

		if (cust.getGender().equalsIgnoreCase("female")) {
			LOGGER.info("El cliente es una mujer :");
			LOGGER.info(cust.toString());
			
			System.out.println("El cliente es una mujer :");
			System.out.println(cust.toString());
		} else {
			LOGGER.info("El cliente es hombre :");
			LOGGER.info(cust.toString());
			System.out.println("El cliente es Hombre :");
			System.out.println(cust.toString());
		}

	}

	@SuppressWarnings("unused")
	private void filterSpecificRFCCustomers() {

	}

}
