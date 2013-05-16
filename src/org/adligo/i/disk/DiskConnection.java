package org.adligo.i.disk;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.adligo.i.log.client.Log;
import org.adligo.i.log.client.LogFactory;
import org.adligo.i.pool.PooledConnection;

public class DiskConnection extends PooledConnection {
	private static final Log log = LogFactory.getLog(DiskConnection.class);
	
	DiskConnection() {
		
	}
	
	public void readFile(String fileName, I_InputProcessor proc) throws FileNotFoundException {
		InputStream in = null;
		try {
			File file = new File(fileName);
			long len = file.length();
			in = new FileInputStream(file);
			proc.process(in, len);
		} catch (IOException x) {
			log.error(x.getMessage(), x);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException x) {
					log.error(x.getMessage(), x);
				} 
			}
		}
	}
	
	public boolean checkIfFileExists(String fileName) {
		File file = new File(fileName);

		if (!file.isFile()) {
			return false;
		}
		return file.exists();
	}
	
	public boolean checkIfDirectoryExists(String fileName) {
		File file = new File(fileName);
		
		if (!file.isDirectory()) {
			return false;
		}
		return file.exists();
	}
	
	public boolean checkIfHidden(String fileName) {
		File file = new File(fileName);
		return file.isHidden();
	}
	
	public Long getFreeSpace(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			return null;
		}
		return file.getFreeSpace();
	}
	
	public Long getUsableSpace(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			return null;
		}
		return file.getUsableSpace();
	}
	
	public Long getModifiedTime(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			return null;
		}
		return file.lastModified();
	}

	@Override
	public boolean isReadWrite() {
		return false;
	}

	@Override
	public boolean isOK() {
		return true;
	}

	@Override
	public void dispose() {
		//do nothing
	}
	
	/**
	 * 
	 * @param dir
	 * @param recurse the number of recursions 
	 * 		ie 0 for listing only the direct children of the directory
	 *         1 for listing only the children and grandchildren of the directory
	 *         -1 for infinite recursion
	 * @return
	 */
	public List<DiskItem> listContents(String dir, int recurse) {
		List<DiskItem> toRet = new ArrayList<DiskItem>();
		File file = new File(dir);
		if (file.isDirectory()) {
			File [] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				File f = files[i];
				toRet.add(new DiskItem(f.isFile(), f.getName(), dir));
				if (f.isDirectory() && recurse != 0) {
					int subcurse = recurse;
					if (recurse >= 1) {
						subcurse = subcurse - 1;
					}
					List<DiskItem> subs = listContents(f.getAbsolutePath(), subcurse);
					toRet.addAll(subs);
				}
			}
		}
		return toRet;
	}
	
	/**
	 * note this will filter out all directories
	 * @param dir
	 * @param filter
	 * @param recurse
	 * @return
	 */
	public List<DiskItem> listContents(String dir, FileFilter filter, int recurse) {
		List<DiskItem> toRet = new ArrayList<DiskItem>();
		File file = new File(dir);
		if (file.isDirectory()) {
			File [] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				File f = files[i];
				if (f.isFile()) {
					if (filter.accept(f)) {
						toRet.add(new DiskItem(f.isFile(), f.getName(), dir));
					}
				}
				if (f.isDirectory() && recurse != 0) {
					int subcurse = recurse;
					if (recurse >= 1) {
						subcurse = subcurse - 1;
					}
					List<DiskItem> subs = listContents(f.getAbsolutePath(), filter, subcurse);
					toRet.addAll(subs);
				}
			}
		}
		return toRet;
	}
}
