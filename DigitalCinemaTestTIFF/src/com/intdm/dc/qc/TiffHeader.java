package com.intdm.dc.qc;

import java.util.ArrayList;

/**
 * @author Tihomir Monov
 *
 */
public class TiffHeader {
	protected short byteOrder = 0x4D4D;
	protected short fileIdentify = 0x002A; // number 42
	protected int IFDOffset = 0x00000008;
	protected String filePath;
	protected ArrayList<ImageFileDirectory> imgFileDirectories;

	public TiffHeader(short byteOrder, short fileIdentify, int offset, String filePath) {
		this.byteOrder = byteOrder;
		this.fileIdentify = fileIdentify;
		IFDOffset = offset;
		this.imgFileDirectories = new ArrayList<ImageFileDirectory>();
		this.filePath = filePath;
	}

	public TiffHeader() {
		this.imgFileDirectories = new ArrayList<ImageFileDirectory>();
		this.filePath = new String();
	}

	public short getByteOrder() {
		return byteOrder;
	}

	public void setByteOrder(short byteOrder) {
		this.byteOrder = byteOrder;
	}

	public short getFileIdentify() {
		return fileIdentify;
	}

	public void setFileIdentify(short fileIdentify) {
		this.fileIdentify = fileIdentify;
	}

	public int getIFDOffset() {
		return IFDOffset;
	}

	public void setIFDOffset(int offset) {
		IFDOffset = offset;
	}


	public void addImgFileDirectories(ImageFileDirectory imgFileDirectory) {
		imgFileDirectories.add(imgFileDirectory);
		imgFileDirectory.setTiffHeader(this);
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
//		sb.append("\n");
		sb.append("File path: " + filePath);
		sb.append("\n");
//		sb.append("Tiff header >");
//		sb.append("\n");
//		sb.append("Byte order: " + byteOrder);
//		sb.append("\n");
//		sb.append("File identify: " + fileIdentify);
//		sb.append("\n");
//		sb.append("IFD offset: " + IFDOffset);
//		sb.append("\n");

		for (int i = 0; i < imgFileDirectories.size(); i++) {
			ArrayList<DirectoryEntry> des = imgFileDirectories.get(i).getDirectoryEntries();
			long size = 0;
			for (int j = 0; j < des.size(); j++) {
//				sb.append("Image file directories > " + (j + 1));
//				sb.append("\n");
//				sb.append("Tag: " + new Long(des.get(j).getTag()).toString());
//				sb.append("\n");
//				sb.append("Type: " + new Long(des.get(j).getType()).toString());
//				sb.append("\n");
//				sb.append("Count: " + new Long(des.get(j).getCount()).toString());
//				sb.append("\n");
//				sb.append("Value or Offset: " + new Long(des.get(j).getOffset()).toString());
//				sb.append("\n");
				if (des.get(j).getTag() == 273 || des.get(j).getTag() == 279) {
					size += des.get(j).getOffset();
				}
			}
			sb.append("Size: " + size + " bytes");
//			sb.append("\n");
		}
		return sb.toString();
	}

	public ArrayList<ImageFileDirectory> getImgFileDirectories() {
		return imgFileDirectories;
	}

}
