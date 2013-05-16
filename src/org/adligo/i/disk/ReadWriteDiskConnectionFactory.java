package org.adligo.i.disk;

import java.io.IOException;

import org.adligo.i.pool.I_PooledConnectionFactory;

public class ReadWriteDiskConnectionFactory implements I_PooledConnectionFactory<ReadWriteDiskConnection> {
	private I_DiskConnectionFactoryConfig<ReadWriteDiskConnection> _config;
	
	public ReadWriteDiskConnectionFactory() {
		_config = new DiskConnectionFactoryConfig<ReadWriteDiskConnection>();
	}
	
	public ReadWriteDiskConnectionFactory(I_DiskConnectionFactoryConfig<ReadWriteDiskConnection> config) {
		_config = config;
	}
	
	@Override
	public ReadWriteDiskConnection create() throws IOException {
		return new ReadWriteDiskConnection(_config);
	}

}
