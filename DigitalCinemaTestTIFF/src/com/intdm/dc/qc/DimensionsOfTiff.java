package com.intdm.dc.qc;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author Tihomir Monov
 *
 */
public class DimensionsOfTiff {


	public static TiffHeader checkTiffHeader(String fileName, long width, long height, long size) throws IOException, InvalidTiffHeaderExeption {
		TiffHeader tiffHeader = new TiffHeader();
		RandomAccessFile raf = new RandomAccessFile(fileName, "r");

		tiffHeader.setFilePath(fileName);
		long byteOrder = Utils.readLeTwoBytesUnsigned(raf);
		tiffHeader.setByteOrder((short) byteOrder);
//		 System.out.format("Byte order: %x%n", byteOrder);
		if (byteOrder != 0x4949) {
			throw new InvalidTiffHeaderExeption("Invalid byte order!");
		}
		raf.seek(2);
		long fIdentify = Utils.readLeTwoBytesUnsigned(raf);
		tiffHeader.setFileIdentify((short) fIdentify);
//		 System.out.format("File identify: %d %n", fIdentify);
		raf.seek(4);
		long IFDoffset = Utils.readLeFourBytesUnsigned(raf);
		tiffHeader.setIFDOffset((int) IFDoffset);
//		 System.out.format("IFD offset: %d%n", IFDoffset);
		raf.seek(IFDoffset);
		long numberOfDirEntries = Utils.readLeTwoBytesUnsigned(raf);
		// System.out.format("Number of directories: %d%n", numberOfDirEntries);
		int counter = 0;

		IFDoffset += 2; // offset number of directories

		ImageFileDirectory ifd = new ImageFileDirectory();
		long deSize = 0;
		for (int i = 0; i < numberOfDirEntries; i++) {
			DirectoryEntry de = new DirectoryEntry(IFDoffset + counter, raf);
//			DirectoryEntry dEntrySize = new DirectoryEntry(IFDoffset, raf);
			if (de.getTag() == 256) {
				if (de.getOffset() != width) {
					throw new InvalidTiffHeaderExeption("Invalid image width for " + tiffHeader.getFilePath());
				}
			}
			if (de.getTag() == 257) {
				if (de.getOffset() != height) {
					throw new InvalidTiffHeaderExeption("Invalid image height for " + tiffHeader.getFilePath());
				}
			}
			if (de.getTag() == 273 || de.getTag() == 279) {
				deSize += de.getOffset();		
			}
			ifd.addDirectoryEntry(de);
			 System.out.println(de.toString());
			counter += 12;
		}
		if (deSize != size) {
			throw new InvalidTiffHeaderExeption("Invalid image size for " + tiffHeader.getFilePath() + "- " + deSize);
		}
		tiffHeader.addImgFileDirectories(ifd);
		raf.close();
		return tiffHeader;
	}
	
}

