package com.intdm.dc.qc;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author Tihomir Monov
 *
 */
public class DirectoryEntry {
	private long tag;
	private long type;
	private long count;
	private long offset;
	private ImageFileDirectory imageFileDirectory;
	public final static int BYTE = 1;
	public final static int ACCII = 2;
	public final static int SHORT = 3;
	public final static int LONG = 4;
	public final static int RATIONAL = 5;
	public final static int SBYTE = 6;
	public final static int UNDEFINED = 7;
	public final static int SSHORT = 8;
	public final static int SLONG = 9;
	public final static int SRATIONAL = 10;
	public final static int FLOAT = 11;
	public final static int DOUBLE = 12;

	public DirectoryEntry(long tag, long type, long count, long offset) {
		this.tag = tag;
		this.type = type;
		this.count = count;
		this.offset = offset;
	}

	public DirectoryEntry(long offset, RandomAccessFile raf) throws IOException {
		raf.seek(offset);
		this.tag = Utils.readLeTwoBytesUnsigned(raf);
		raf.seek(offset + 2);
		this.type = Utils.readLeTwoBytesUnsigned(raf);
		raf.seek(offset + 4);
		this.count = Utils.readLeFourBytesUnsigned(raf);
		raf.seek(offset + 8);
		this.offset = Utils.readLeFourBytesUnsigned(raf);
	}

	/*public DirectoryEntry() {
		this(0, 0, 0, 0);
	}*/

	public long getTag() {
		return tag;
	}

	public void setTag(long tag) {
		this.tag = tag;
	}

	public long getType() {
		return type;
	}

	public void setType(long type) {
		this.type = type;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	@Override
	public String toString() {
		return "tag, type, count, offset:" + this.tag + ", " + this.type + ", " + this.count + ", " + this.offset;
	}

	public ImageFileDirectory getImageFileDirectory() {
		return imageFileDirectory;
	}

	public void setImageFileDirectory(ImageFileDirectory imageFileDirectory) {
		this.imageFileDirectory = imageFileDirectory;
	}

}
