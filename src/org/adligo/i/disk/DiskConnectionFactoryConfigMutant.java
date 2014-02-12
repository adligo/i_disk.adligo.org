package org.adligo.i.disk;

import org.adligo.i.pool.I_PooledConnectionFactory;
import org.adligo.i.pool.PoolConfigurationMutant;
import org.adligo.models.core.shared.InvalidParameterException;

public class DiskConnectionFactoryConfigMutant<T extends DiskConnection> extends PoolConfigurationMutant<T> implements I_DiskConnectionFactoryConfig<T> {
	/**
	 * this is the number of bytes between stream reads or writes
	 * before the connection is marked active
	 */
	private int streamBytesBetweenActive = 512;

	public DiskConnectionFactoryConfigMutant() {}
	
	public DiskConnectionFactoryConfigMutant(I_DiskConnectionFactoryConfig<T> other) throws InvalidParameterException {
		super(other);
		
	}
	
	
	public DiskConnectionFactoryConfigMutant(String name, I_PooledConnectionFactory<T> factory, int max)
			throws InvalidParameterException {
		super(name, factory, max);
	}
	
	/* (non-Javadoc)
	 * @see org.adligo.i.disk.I_DiskConnectionFactoryConfig#getStreamBytesBetweenActive()
	 */
	@Override
	public int getStreamBytesBetweenActive() {
		return streamBytesBetweenActive;
	}

	public void setStreamBytesBetweenActive(int streamBytesBetweenActive) {
		this.streamBytesBetweenActive = streamBytesBetweenActive;
	}
}
