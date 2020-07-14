package com.poc.spark.db;

import java.io.Serializable;

public interface ConnectionPool<T> extends Serializable {

	public abstract T getConnection();

	public void returnConnection(T conn);

	public void invalidateConnection(T conn);

}
