package com.poc.spark.db;

import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Connection;

public class HbaseConnectionFactory implements ConnectionFactory<Connection> {

	private static final long serialVersionUID = 4024923894283696465L;

	private final Configuration hadoopConfiguration;

	public HbaseConnectionFactory(final Configuration hadoopConfiguration) {

		this.hadoopConfiguration = hadoopConfiguration;
	}

	public HbaseConnectionFactory(final String host, final String port, final String master, final String rootdir) {

		this.hadoopConfiguration = new Configuration();

		if (host == null)
			throw new ConnectionException("[" + HbaseConfig.ZOOKEEPER_QUORUM_PROPERTY + "] is required !");
		this.hadoopConfiguration.set(HbaseConfig.ZOOKEEPER_QUORUM_PROPERTY, host);

		if (port == null)
			throw new ConnectionException("[" + HbaseConfig.ZOOKEEPER_CLIENTPORT_PROPERTY + "] is required !");
		this.hadoopConfiguration.set(HbaseConfig.ZOOKEEPER_CLIENTPORT_PROPERTY, port);

		if (master != null)
			this.hadoopConfiguration.set(HbaseConfig.MASTER_PROPERTY, master);

		if (rootdir != null)
			this.hadoopConfiguration.set(HbaseConfig.ROOTDIR_PROPERTY, rootdir);
	}

	public HbaseConnectionFactory(final Properties properties) {

		this.hadoopConfiguration = new Configuration();

		for (Entry<Object, Object> entry : properties.entrySet()) {

			this.hadoopConfiguration.set((String) entry.getKey(), (String) entry.getValue());
		}
	}

	@Override
	public PooledObject<Connection> makeObject() throws Exception {

		Connection connection = this.createConnection();

		return new DefaultPooledObject<Connection>(connection);
	}

	@Override
	public void destroyObject(PooledObject<Connection> p) throws Exception {

		Connection connection = p.getObject();

		if (connection != null)

			connection.close();
	}

	@Override
	public boolean validateObject(PooledObject<Connection> p) {

		Connection connection = p.getObject();

		if (connection != null)

			return ((!connection.isAborted()) && (!connection.isClosed()));

		return false;
	}

	@Override
	public void activateObject(PooledObject<Connection> p) throws Exception {

	}

	@Override
	public void passivateObject(PooledObject<Connection> p) throws Exception {

	}

	@Override
	public Connection createConnection() throws Exception {

		Connection connection = org.apache.hadoop.hbase.client.ConnectionFactory.createConnection(hadoopConfiguration);

		return connection;
	}

}
