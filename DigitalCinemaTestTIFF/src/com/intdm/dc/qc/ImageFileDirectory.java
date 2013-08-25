package com.intdm.dc.qc;

import java.util.ArrayList;

/**
 * @author Tihomir Monov
 *
 */
public class ImageFileDirectory {
//	private int numberOfDirectoryEntries;
	private ArrayList<DirectoryEntry> directoryEntries;
//	private TiffHeader tiffHeader;

	public ImageFileDirectory(int numberOfDirectoryEntries) {
//		this.numberOfDirectoryEntries = numberOfDirectoryEntries;
		this.directoryEntries = new ArrayList<DirectoryEntry>(numberOfDirectoryEntries);
	}

	public ImageFileDirectory() {
		this(0);
	}

//	public int getNumberOfDirectoryEntries() {
//		return numberOfDirectoryEntries;
//	}
//
//	public void setNumberOfDirectoryEntries(int numberOfDirectoryEntries) {
//		this.numberOfDirectoryEntries = numberOfDirectoryEntries;
//	}

	public ArrayList<DirectoryEntry> getDirectoryEntries() {
		return directoryEntries;
	}

	public void addDirectoryEntry(DirectoryEntry directoryEntry) {
		directoryEntries.add(directoryEntry);
//		directoryEntry.setImageFileDirectory(this);
	}

//	public TiffHeader getTiffHeader() {
//		return tiffHeader;
//	}
//
//	public void setTiffHeader(TiffHeader tiffHeader) {
//		this.tiffHeader = tiffHeader;
//	}
//	@Override
//		public boolean equals(Object obj) {
//		if(obj instanceof ImageFileDirectory) {
//			ImageFileDirectory ifd = (ImageFileDirectory)obj;
//			return directoryEntries == ifd.getDirectoryEntries();
//			}
//			return false;
//		}
}
