package org.adligo.i.disk;

public class DiskItem {
	/**
	 * true if this is a file
	 * false if this is a directory
	 */
	private boolean _file;
	/**
	 * the simple right most name of the file or directory
	 */
	private String _name;
	/**
	 * the absolute path of this disk item 
	 * (directory above the file or directory that this object represents)
	 */
	private String _path;
	
	public DiskItem(boolean file, String name, String path) {
		_file = file;
		_name = name;
		_path = path;
	}

	public boolean isDirectory() {
		return !_file;
	}
	
	public boolean isFile() {
		return _file;
	}

	public String getName() {
		return _name;
	}

	public String getPath() {
		return _path;
	}
}
