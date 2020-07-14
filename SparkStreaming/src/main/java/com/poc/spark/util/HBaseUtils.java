package com.poc.spark.util;

import java.io.Serializable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.springframework.stereotype.Component;

import com.poc.spark.constant.Constants;
import com.poc.spark.db.ConnectionPoolConfig;
import com.poc.spark.db.HbaseConnectionPool;
import com.poc.spark.domain.config.ConfigurationManager;

@Component
public class HBaseUtils  implements Serializable{

	private static final long serialVersionUID = 1L;
	private static HbaseConnectionPool pool;
	
	
	static {
		ConnectionPoolConfig config = new ConnectionPoolConfig();
		config.setMaxTotal(ConfigurationManager.getInteger(Constants.HBASE_POOL_MAX_TOTAL));
		config.setMaxIdle(ConfigurationManager.getInteger(Constants.HBASE_POOL_MAX_IDLE));
		config.setMaxWaitMillis(ConfigurationManager.getInteger(Constants.HBASE_POOL_MAX_WAITMILLIS));
		config.setTestOnBorrow(ConfigurationManager.getBoolean(Constants.HBASE_POOL_TESTONBORROW));
		Configuration hbaseConfig = getHBaseConfiguration();
		pool = new HbaseConnectionPool(config, hbaseConfig);
	}
	
	public static Configuration getHBaseConfiguration() {
		Configuration conf = null;
		try {
			conf = HBaseConfiguration.create();

		} catch (Exception e) {

		}
		return conf;
	}

	public static synchronized Connection getConn() {
		return pool.getConnection();
	}


}
