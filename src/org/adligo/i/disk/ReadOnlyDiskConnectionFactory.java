package org.adligo.i.disk;

import java.io.IOException;

import org.adligo.i.pool.I_PooledConnectionFactory;

public class ReadOnlyDiskConnectionFactory implements I_PooledConnectionFactory<ReadOnlyDiskConnection> {
	private I_DiskConnectionFactoryConfig<ReadOnlyDiskConnection> _config;
	
	public ReadOnlyDiskConnectionFactory() {
		_config = new DiskConnectionFactoryConfig<ReadOnlyDiskConnection>();
	}
	
	public ReadOnlyDiskConnectionFactory(I_DiskConnectionFactoryConfig<ReadOnlyDiskConnection> config) {
		_config = config;
	}
	
	@Override
	public ReadOnlyDiskConnection create() throws IOException {
		return new ReadOnlyDiskConnection(_config);
	}

}
