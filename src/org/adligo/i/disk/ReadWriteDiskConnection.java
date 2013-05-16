package org.adligo.i.disk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.adligo.i.log.client.Log;
import org.adligo.i.log.client.LogFactory;

public class ReadWriteDiskConnection  extends DiskConnection {
	private static final Log log = LogFactory.getLog(ReadWriteDiskConnection.class);
	
	public boolean createDirectories(String fileName) {
		File file = new File(fileName);
		return file.mkdirs();
	}
	
	public void writeFile(String fileName, I_OutputProcessor proc) throws IOException {
		File file = new File(fileName);
		if (!file.exists()) {
			if (!file.createNewFile()) {
				throw new IOException("Unable to create file " + fileName);
			}
		}
		
		OutputStream out = null;
		try {
			out = new FileOutputStream(file);
			proc.process(out);
		} catch (IOException x) {
			log.error(x.getMessage(), x);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException x) {
					log.error(x.getMessage(), x);
				} 
			}
		}
	}
	
	@Override
	public boolean isReadWrite() {
		return true;
	}
}
