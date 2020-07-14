package com.poc.spark.domain;

import java.io.IOException;
import java.io.Serializable;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.poc.spark.domain.hdfs.CustomersSerDes;
import com.poc.spark.model.Customers;
import com.poc.spark.util.JsonMapperUtil;

@Controller
public class CustomersRDD implements Serializable {
	private static final Logger LOGGER = LogManager.getLogger(CustomersRDD.class);

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private JsonMapperUtil jsonMapperUtil;
	
	@Autowired
	private CustomersSerDes customersSerDes;

	public void startCustomersProccesor(JavaDStream<String> customers) {
		LOGGER.warn("startCustomersProccesor ...");
		
		JavaDStream<Customers> customers2 = customers.map(f-> {
			Customers cust = jsonMapperUtil.rowToModel(f);
			LOGGER.warn("Retornar  : JavaDStream<Customers>");
			return  cust;
		});
		
		JavaDStream<Customers> custmajor35 =  customers2.filter(f -> f.getAge() >30);
		custmajor35.foreachRDD(rdd -> customersSerDes.saveRDD(rdd, "custmajor35"));
		
		JavaDStream<Customers> custbyRFC =  customers2.filter(f -> f.getRfc().equals("AAAA0101015L6"));
		custbyRFC.foreachRDD(rdd -> customersSerDes.saveRDD(rdd, "custbyRFC"));

		customers.foreachRDD(rdd -> rdd.foreach(x -> {
			filteGenderCustomers(x);
			filterSpecificRFCCustomers(x);
		}));
	}

	private void filteGenderCustomers(String row) throws  IOException {

		Customers cust = jsonMapperUtil.rowToModel(row);

		if (cust.getGender().equals("FEMALE")) {
			LOGGER.warn("El cliente es una mujer :");
			LOGGER.warn(cust.toString());						
		} else {
			LOGGER.warn("El cliente es hombre :");
			LOGGER.warn(cust.toString());			
		}
	}

	private void filterSpecificRFCCustomers(String row) throws IOException {
		Customers cust = jsonMapperUtil.rowToModel(row);
		if (cust.getRfc().equals("AAAA0101015L6")) {
			LOGGER.warn("Es de un cliente perfilado :" +cust.toString());
		}
	}
}
