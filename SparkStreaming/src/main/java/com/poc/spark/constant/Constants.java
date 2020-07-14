package com.poc.spark.constant;

import java.text.SimpleDateFormat;

public final class Constants {
	

	public static final String ZK_METADATA_BROKER_LIST = "zk.metadata.broker.list";

	public static final String CF_DEFAULT = "cf-txn";
	public static final String DEFAULT_ROW_KEY = "_pk";
	public static final String HBASE_POOL_MAX_TOTAL = "hbase.pool.max-total";
	public static final String HBASE_POOL_MAX_IDLE = "hbase.pool.max-idle";
	public static final String HBASE_POOL_MAX_WAITMILLIS = "hbase.pool.max-waitmillis";
	public static final String HBASE_POOL_TESTONBORROW = "hbase.pool.testonborrow";
	
	public static final	String SEPARATOR_001 = "\001";
	public static final String SEPARATOR_002 = "\002";
	public static final String ENCODE_UTF8 = "UTF-8";
	public static final String DECODE_UTF8 = "UTF-8";
	
	public static final String IDCard_BACK_x = "x";
	public static final String IDCard_BACK_X = "X";
	public static final String IDCard_BACK_11 = "11";
	public static final String STR_NUMBER_0 = "0";

	public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat DATEKEY_FORMAT = new SimpleDateFormat("yyyyMMdd");

	public static final String JAR_LOG_PATH = "jar.log.path";
	public static final String BASE_DIR_OUTPUT_W ="C:\\tmp\\poc\\avro\\";
	public static final String BASE_DIR_OUTPUT_HDFS ="/tmp/poc/";
	public static final String WINDOWS_SEPARATOR ="\\";
	public static final char UNIX_SEPARATOR = '/';


}
