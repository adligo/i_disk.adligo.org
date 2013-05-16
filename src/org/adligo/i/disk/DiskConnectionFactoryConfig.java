package org.adligo.i.disk;

import org.adligo.i.pool.PoolConfigurationMutant;
import org.adligo.models.core.client.InvalidParameterException;

public class DiskConnectionFactoryConfig<T extends DiskConnection> extends PoolConfigurationMutant<T> 
	implements I_DiskConnectionFactoryConfig<T> {
	private DiskConnectionFactoryConfigMutant<T> mutant;

	public DiskConnectionFactoryConfig() {
		mutant = new DiskConnectionFactoryConfigMutant<T>();
	}
	
	public DiskConnectionFactoryConfig(I_DiskConnectionFactoryConfig<T> config) throws InvalidParameterException {
		mutant = new DiskConnectionFactoryConfigMutant<T>(config);
	}
	
	public int getStreamBytesBetweenActive() {
		return mutant.getStreamBytesBetweenActive();
	}
	
}
