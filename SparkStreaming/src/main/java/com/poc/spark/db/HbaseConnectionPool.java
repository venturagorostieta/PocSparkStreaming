package com.poc.spark.db;

import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Connection;

public class HbaseConnectionPool extends ConnectionPoolBase<Connection> implements ConnectionPool<Connection> {

	private static final long serialVersionUID = -9126420905798370243L;

	public HbaseConnectionPool() {

		this(HbaseConfig.DEFAULT_HOST, HbaseConfig.DEFAULT_PORT);
	}

	public HbaseConnectionPool(final String host, final String port) {

		this(new ConnectionPoolConfig(), host, port, HbaseConfig.DEFAULT_MASTER, HbaseConfig.DEFAULT_ROOTDIR);
	}

	public HbaseConnectionPool(final String host, final String port, final String master, final String rootdir) {

		this(new ConnectionPoolConfig(), host, port, master, rootdir);
	}

	public HbaseConnectionPool(final Configuration hadoopConfiguration) {

		this(new ConnectionPoolConfig(), hadoopConfiguration);
	}

	public HbaseConnectionPool(final ConnectionPoolConfig poolConfig, final String host, final String port) {

		this(poolConfig, host, port, HbaseConfig.DEFAULT_MASTER, HbaseConfig.DEFAULT_ROOTDIR);
	}

	public HbaseConnectionPool(final ConnectionPoolConfig poolConfig, final Configuration hadoopConfiguration) {

		super(poolConfig, new HbaseConnectionFactory(hadoopConfiguration));
	}

	public HbaseConnectionPool(final ConnectionPoolConfig poolConfig, final String host, final String port,
			final String master, final String rootdir) {

		super(poolConfig, new HbaseConnectionFactory(host, port, master, rootdir));
	}

	public HbaseConnectionPool(final ConnectionPoolConfig poolConfig, final Properties properties) {

		super(poolConfig, new HbaseConnectionFactory(properties));
	}

	@Override
	public Connection getConnection() {

		return super.getResource();
	}

	@Override
	public void returnConnection(Connection conn) {

		super.returnResource(conn);
	}

	@Override
	public void invalidateConnection(Connection conn) {

		super.invalidateResource(conn);
	}

}
