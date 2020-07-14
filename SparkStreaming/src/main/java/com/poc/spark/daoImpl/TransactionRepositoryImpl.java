package com.poc.spark.daoImpl;

import java.io.IOException;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Repository;

import com.poc.spark.dao.TransactionRepository;
import com.poc.spark.domain.config.DomainConf;
import com.poc.spark.model.Transactions;
import com.poc.spark.util.HBaseUtils;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository{

	private static final Logger LOGGER = LogManager.getLogger(TransactionRepositoryImpl.class);

	private  Connection conn;
	
	private HBaseUtils hBaseUtils;

public static void main(String args []) {
	
	AnnotationConfigApplicationContext factoria = new AnnotationConfigApplicationContext();
	factoria.register(DomainConf.class);
	factoria.refresh();

	TransactionRepositoryImpl app = factoria.getBean(TransactionRepositoryImpl.class);

	Transactions obj = new Transactions();
	obj.setCompany("TestDao");
	obj.setCurrency("USD");
	obj.setCustomerid(2);
	obj.setTotal(400.60);
	obj.setTxnid(10);
	obj.setTxnts(null);
	
	app.saveTransactions(obj);
}
	@Override
	public void saveTransactions(Transactions txn) {
		Table table = null;
		try {
			conn = hBaseUtils.getConn();
			try {
				table = conn.getTable(TableName.valueOf("transactions"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			Put put1 = new Put(Bytes.toBytes(3));
			put1.addColumn(Bytes.toBytes("cf-txn"), Bytes.toBytes("txnts"), Bytes.toBytes("45454545454"));
			put1.addColumn(Bytes.toBytes("cf-txn"), Bytes.toBytes("total"), Bytes.toBytes(150.2));
			put1.addColumn(Bytes.toBytes("cf-txn"), Bytes.toBytes("company"), Bytes.toBytes("TCMP"));
			put1.addColumn(Bytes.toBytes("cf-txn"), Bytes.toBytes("currency"), Bytes.toBytes("MXN"));
			put1.addColumn(Bytes.toBytes("cf-txn"), Bytes.toBytes("customerid"), Bytes.toBytes(2));
			table.put(put1);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				table.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}