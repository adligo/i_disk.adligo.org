package org.adligo.i.disk;

import java.io.IOException;
import java.io.InputStream;

/**
 * this interface allows buffering from large files
 * @author scott
 *
 */
public interface I_InputProcessor {

	public void process(InputStream p, long byteLength) throws IOException;
}
