package com.intdm.dc.fixtures;

import java.io.IOException;

import com.intdm.dc.qc.DimensionsOfTiff;
import com.intdm.dc.qc.InvalidTiffHeaderExeption;

import fit.Fixture;

public class TiffHeaderFixture extends Fixture {
	private String fileName;
	private long width;
	private long height;
	private long size;
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getWidth() {
		return width;
	}

	public void setWidth(long width) {
		this.width = width;
	}

	public long getHeight() {
		return height;
	}

	public void setHeight(long height) {
		this.height = height;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public boolean checkTiffHeaderAttr(){
		boolean result = true;
		try {
			DimensionsOfTiff.checkTiffHeader(fileName, width, height, size);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (InvalidTiffHeaderExeption e) {
			e.printStackTrace();
			return false;
		}
		return result;
	}
	
}
