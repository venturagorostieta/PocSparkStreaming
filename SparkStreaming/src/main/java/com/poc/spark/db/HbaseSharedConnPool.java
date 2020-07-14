package com.poc.spark.db;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Connection;

public class HbaseSharedConnPool implements ConnectionPool<Connection> {

	private static final long serialVersionUID = 1L;

	private static final AtomicReference<HbaseSharedConnPool> pool = new AtomicReference<HbaseSharedConnPool>();

	private final Connection connection;

	private HbaseSharedConnPool(Configuration configuration) throws IOException {
		this.connection = org.apache.hadoop.hbase.client.ConnectionFactory.createConnection(configuration);
	}

	public synchronized static HbaseSharedConnPool getInstance(final String host, final String port,
			final String master, final String rootdir) {

		Properties properties = new Properties();

		if (host == null)
			throw new ConnectionException("[" + HbaseConfig.ZOOKEEPER_QUORUM_PROPERTY + "] is required !");
		properties.setProperty(HbaseConfig.ZOOKEEPER_QUORUM_PROPERTY, host);

		if (port == null)
			throw new ConnectionException("[" + HbaseConfig.ZOOKEEPER_CLIENTPORT_PROPERTY + "] is required !");
		properties.setProperty(HbaseConfig.ZOOKEEPER_CLIENTPORT_PROPERTY, port);

		if (master != null)
			properties.setProperty(HbaseConfig.MASTER_PROPERTY, master);

		if (rootdir != null)
			properties.setProperty(HbaseConfig.ROOTDIR_PROPERTY, rootdir);

		return getInstance(properties);
	}

	public synchronized static HbaseSharedConnPool getInstance(final Properties properties) {

		Configuration configuration = new Configuration();

		for (Map.Entry<Object, Object> entry : properties.entrySet()) {

			configuration.set((String) entry.getKey(), (String) entry.getValue());
		}

		return getInstance(configuration);
	}

	public synchronized static HbaseSharedConnPool getInstance(final Configuration configuration) {

		if (pool.get() == null)

			try {
				pool.set(new HbaseSharedConnPool(configuration));

			} catch (IOException e) {

				e.printStackTrace();
			}

		return pool.get();
	}

	@Override
	public Connection getConnection() {

		return connection;
	}

	@Override
	public void returnConnection(Connection conn) {

	}

	@Override
	public void invalidateConnection(Connection conn) {

		try {
			if (conn != null)

				conn.close();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	/**
	 * Close.
	 */
	public void close() {

		try {
			connection.close();

			pool.set(null);

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

}
