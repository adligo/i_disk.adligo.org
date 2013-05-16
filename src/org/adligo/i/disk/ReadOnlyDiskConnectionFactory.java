package org.adligo.i.disk;

import java.io.IOException;

import org.adligo.i.pool.I_PooledConnectionFactory;

public class ReadOnlyDiskConnectionFactory implements I_PooledConnectionFactory<ReadOnlyDiskConnection> {

	@Override
	public ReadOnlyDiskConnection create() throws IOException {
		return new ReadOnlyDiskConnection();
	}

}
