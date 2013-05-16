package org.adligo.i.disk;

import org.adligo.i.pool.I_PoolConfiguration;

public interface I_DiskConnectionFactoryConfig<T extends DiskConnection> extends 
	I_PoolConfiguration<T> {

	public abstract int getStreamBytesBetweenActive();

}