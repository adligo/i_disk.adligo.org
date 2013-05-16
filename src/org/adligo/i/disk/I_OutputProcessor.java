package org.adligo.i.disk;

import java.io.IOException;
import java.io.OutputStream;
/**
 * this interface allows buffering to large files
 * @author scott
 *
 */
public interface I_OutputProcessor {
	public void process(OutputStream p) throws IOException;
}
