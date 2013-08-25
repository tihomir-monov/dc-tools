package com.intdm.dc.qc;

public class InvalidTiffHeaderExeption extends Exception {


	/**
	 * 
	 */
	private static final long serialVersionUID = -239577193939888298L;

	public InvalidTiffHeaderExeption(String message) {
		super(message);
	}

	public InvalidTiffHeaderExeption(String message, Throwable cause) {
		super(message, cause);
	}
}
