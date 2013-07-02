package com.intdm.dc.qc;

import java.io.IOException;

import java.io.RandomAccessFile;

/**
 * @author Tihomir Monov
 *
 */
public class Utils {
	
	
	public static long readLeTwoBytesUnsigned(RandomAccessFile f) throws IOException {
		return (long) f.readUnsignedByte() | (long) f.readUnsignedByte() << 8;
	}
	
	public static long readLeFourBytesUnsigned(RandomAccessFile f) throws IOException {
		return (long) f.readUnsignedByte() | (long) f.readUnsignedByte() << 8
				| (long) f.readUnsignedByte() << 16
				| (long) f.readUnsignedByte() << 24;
	}
	
	public static long readLeTwelveBytesUnsigned(RandomAccessFile f) throws IOException {
		return (long) f.readUnsignedByte() | (long) f.readUnsignedByte() << 8
				| (long) f.readUnsignedByte() << 16
				| (long) f.readUnsignedByte() << 24
				| (long) f.readUnsignedByte() << 32
				| (long) f.readUnsignedByte() << 40
				| (long) f.readUnsignedByte() << 48
				| (long) f.readUnsignedByte() << 56
				| (long) f.readUnsignedByte() << 64
				| (long) f.readUnsignedByte() << 72
				| (long) f.readUnsignedByte() << 80
				| (long) f.readUnsignedByte() << 88;
	}
}
