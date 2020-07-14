package com.poc.spark.db;

import java.io.Serializable;

import org.apache.commons.pool2.PooledObjectFactory;

public interface ConnectionFactory<T> extends PooledObjectFactory<T>, Serializable {

	public abstract T createConnection() throws Exception;

}
