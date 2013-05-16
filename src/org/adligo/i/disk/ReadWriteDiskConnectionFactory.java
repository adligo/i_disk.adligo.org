package org.adligo.i.disk;

import java.io.IOException;

import org.adligo.i.pool.I_PooledConnectionFactory;

public class ReadWriteDiskConnectionFactory implements I_PooledConnectionFactory<ReadWriteDiskConnection> {

	@Override
	public ReadWriteDiskConnection create() throws IOException {
		return new ReadWriteDiskConnection();
	}

}
