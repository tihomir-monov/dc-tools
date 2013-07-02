package com.intdm.dc.qc;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Pattern;

/**
 * @author Tihomir Monov
 *
 */
public class DimensionsOfTiff {

	/*
	 * public static ArrayList<TiffHeader> getTiffHeaders(String
	 * startDirectory, long width, long height) throws IOException { ArrayList<TiffHeader>
	 * tiffHeaders = new ArrayList<TiffHeader>(); Queue<File> directories =
	 * new LinkedList<File>(); File file = new File(startDirectory);
	 * directories.offer(file); while (directories.size() != 0) { File
	 * currentDirectory = directories.poll(); //
	 * System.out.println(currentDirectory); File[] tiffFiles =
	 * currentDirectory.listFiles(new FileFilter() { public boolean accept(File
	 * pathname) { boolean result = Pattern.matches("\\S+\\.tif$", pathname
	 * .getPath()); return result; } }); for (File f : tiffFiles) { if
	 * (f.isFile()) { tiffHeaders.add(getTiffHeader(f.getAbsolutePath(), width,
	 * height)); } } File[] subdirectories = currentDirectory.listFiles(); for
	 * (File f : subdirectories) { if (f.isDirectory()) { directories.offer(f); } } }
	 * return tiffHeaders; }
	 */

	public static TiffHeader getTiffHeader(String fileName, long width, long height, long size) throws IOException {
		TiffHeader tiffHeader = new TiffHeader();
		RandomAccessFile raf = new RandomAccessFile(fileName, "r");

		tiffHeader.setFilePath(fileName);
		long byteOrder = Utils.readLeTwoBytesUnsigned(raf);
		tiffHeader.setByteOrder((short) byteOrder);
		// System.out.format("Byte order: %x%n", byteOrder);
		if (byteOrder != 0x4949) {
			throw new IOException("Invalid byte order!");
		}
		raf.seek(2);
		long fIdentify = Utils.readLeTwoBytesUnsigned(raf);
		tiffHeader.setFileIdentify((short) fIdentify);
		// System.out.format("File identify: %d %n", fIdentify);
		raf.seek(4);
		long IFDoffset = Utils.readLeFourBytesUnsigned(raf);
		tiffHeader.setIFDOffset((int) IFDoffset);
		// System.out.format("IFD offset: %d%n", IFDoffset);
		// for (int i = 0; i < tiffHeader.getImgFileDirectories().size(); i++) {
		// ArrayList<DirectoryEntry> des =
		// tiffHeader.getImgFileDirectories().get(i).getDirectoryEntries();
		// long deSize = 0;
		// for (int j = 0; j < des.size(); j++) {
		// if (des.get(j).getTag() == 273 || des.get(j).getTag() == 279) {
		// deSize += des.get(j).getOffset();
		// if (deSize != size) {
		// throw new IOException("Invalid image size for " +
		// tiffHeader.getFilePath() + " deSize: " + deSize);
		// }
		// }
		// }
		// }
		raf.seek(IFDoffset);
		long numberOfDirEntries = Utils.readLeTwoBytesUnsigned(raf);
		// System.out.format("Number of directories: %d%n", numberOfDirEntries);
		int counter = 0;

		IFDoffset += 2; // offset number of directories

		ImageFileDirectory ifd = new ImageFileDirectory();
		long deSize = 0;
		for (int i = 0; i < numberOfDirEntries; i++) {
			DirectoryEntry de = new DirectoryEntry(IFDoffset + counter, raf);
			DirectoryEntry dEntrySize = new DirectoryEntry(IFDoffset, raf);
			if (de.getTag() == 256) {
				if (de.getOffset() != width) {
					throw new IOException("Invalid image width for " + tiffHeader.getFilePath());
				}
			}
			if (de.getTag() == 257) {
				if (de.getOffset() != height) {
					throw new IOException("Invalid image height for " + tiffHeader.getFilePath());
				}
			}
			if (de.getTag() == 273 || de.getTag() == 279) {
				deSize += de.getOffset();		
			}
			ifd.addDirectoryEntry(de);
			// System.out.println(de.toString());
			counter += 12;
		}
		if (deSize != size) {
			throw new IOException("Invalid image size for " + tiffHeader.getFilePath() + "- " + deSize);
		}
		ifd.setNumberOfDirectoryEntries((int) numberOfDirEntries);
		tiffHeader.addImgFileDirectories(ifd);
		raf.close();
		return tiffHeader;
	}

}
