package com.poc.spark.domain;

import java.io.Serializable;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.poc.spark.domain.hdfs.TransactionsSerDes;
import com.poc.spark.model.Transactions;
import com.poc.spark.util.JsonMapperUtil;

@Controller
public class TransactionsRDD   implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger(TransactionsRDD.class);

	@Autowired
	private JsonMapperUtil jsonMapperUtil;
	
	@Autowired
	private TransactionsSerDes transactionsSerDes;

	public void startTransactionsProccesor(JavaDStream<String> transactions) {
		
		LOGGER.warn("startTransactionsProccesor ...");
		JavaDStream<Transactions> transactionsDS = transactions.map(f-> {
			Transactions txn = jsonMapperUtil.rowToTransaction(f);
			LOGGER.warn("Retornar  : JavaDStream<Transactions>");
			return  txn;
		});
				
		JavaDStream<Transactions> amountFilter = transactionsDS.filter(f -> f.getTotal() >100);		
		amountFilter.foreachRDD(rdd -> transactionsSerDes.saveRDD(rdd,"amountFilter"));		
		
		JavaDStream<Transactions> txnCurrency = transactionsDS.filter(f -> f.getCurrency().equals("MXN"));
		txnCurrency.foreachRDD(rdd -> transactionsSerDes.saveRDD(rdd,"txnCurrency"));
		
		JavaDStream<Transactions> txnCompany = transactionsDS.filter(f -> f.getCompany().equals("VenturSoft"));
		txnCompany.foreachRDD(rdd -> transactionsSerDes.saveRDD(rdd,"txnCompany"));
	}

}
