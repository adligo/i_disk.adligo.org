package org.adligo.i.disk;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.adligo.i.log.shared.Log;
import org.adligo.i.log.shared.LogFactory;
import org.adligo.i.pool.I_Pool;
import org.adligo.i.pool.I_PooledConnection;
import org.adligo.i.pool.PooledConnection;

public class DiskConnection extends PooledConnection {
	public static final String NO_MORE_BYTES_ARE_AVAILABLE = "No more bytes are available.";
	public static final String A_DISK_CONNECTION_MAY_ONLY_STREAM_ONE_FILE_AT_A_TIME = "A DiskConnection may only stream one file at a time!";
	private static final Log log = LogFactory.getLog(DiskConnection.class);
	private InputStream stream;
	private Byte nextByte;
	private int streamBytesBetweenImActive;
	private int bytesStreamed;
	
	DiskConnection(I_DiskConnectionFactoryConfig<? extends DiskConnection> p) {
		streamBytesBetweenImActive = p.getStreamBytesBetweenActive();
	}
	
	 
	/**
	 * use for small files,
	 * for larger files you may want to consider using a
	 * stream read
	 * @param fileName
	 * @param proc
	 * @throws FileNotFoundException
	 */
	public void readFile(String fileName, I_InputProcessor proc) throws FileNotFoundException {
		super.markActive();
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
		super.markActive();
		File file = new File(fileName);

		if (!file.isFile()) {
			return false;
		}
		return file.exists();
	}
	
	public boolean checkIfDirectoryExists(String fileName) {
		super.markActive();
		File file = new File(fileName);
		
		if (!file.isDirectory()) {
			return false;
		}
		return file.exists();
	}
	
	public boolean checkIfHidden(String fileName) {
		super.markActive();
		File file = new File(fileName);
		return file.isHidden();
	}
	
	public Long getFreeSpace(String fileName) {
		super.markActive();
		File file = new File(fileName);
		if (!file.exists()) {
			return null;
		}
		return file.getFreeSpace();
	}
	
	public Long getUsableSpace(String fileName) {
		super.markActive();
		File file = new File(fileName);
		if (!file.exists()) {
			return null;
		}
		return file.getUsableSpace();
	}
	
	public Long getModifiedTime(String fileName) {
		super.markActive();
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
		endStreamRead();
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
		super.markActive();
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
		super.markActive();
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



	/**
	 * @see I_PooledConnection#returnToPool()
	 * this also closes the stream read if it was in process.
	 */
	@Override
	public void returnToPool() {
		endStreamRead();
		super.returnToPool();
	}

	public void startStreamRead(String fileName) throws IOException {
		if (stream != null) {
			throw new IOException(A_DISK_CONNECTION_MAY_ONLY_STREAM_ONE_FILE_AT_A_TIME);
		}
		super.markActive();
		try {
			stream = new FileInputStream(fileName);
		} catch (FileNotFoundException x) {
			throw new IOException(x);
		}
	}
	
	public boolean hasMoreBytes() throws IOException {
		if (nextByte != null) {
			return true;
		} else {
			return assignNextByte();
		}
	}


	private boolean assignNextByte() throws IOException {
		int next = stream.read();
		if (next != -1) {
			bytesStreamed++;
			if (bytesStreamed >= streamBytesBetweenImActive) {
				bytesStreamed = 0;
				super.markActive();
			}
			nextByte = (byte) next;
			return true;
		}
		return false;
	}

	public byte nextByte() throws IOException {
		if (nextByte == null) {
			if (!assignNextByte()) {
				throw new IOException(NO_MORE_BYTES_ARE_AVAILABLE);
			}
		}
		Byte toRet = nextByte;
		nextByte = null;
		return toRet;
	}
	
	public void endStreamRead() {
		super.markActive();
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException x) {
				log.error(x.getMessage(), x);
			}
		}
		stream = null;
		nextByte = null;
	}
}
