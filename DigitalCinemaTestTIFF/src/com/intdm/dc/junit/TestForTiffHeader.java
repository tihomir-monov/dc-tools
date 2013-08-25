/**
 * 
 */
package com.intdm.dc.junit;

import static org.junit.Assert.*;
//import static org.hamcrest.CoreMatchers.is;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.intdm.dc.qc.DimensionsOfTiff;
import com.intdm.dc.qc.DirectoryEntry;
import com.intdm.dc.qc.ImageFileDirectory;
import com.intdm.dc.qc.InvalidTiffHeaderExeption;
import com.intdm.dc.qc.TiffHeader;

/**
 * @author Tihomir Monov
 *
 */
public class TestForTiffHeader {
	
	private TiffHeader tiffHeader;
	private static final String FILE_NAME = "../../../intdm/dc/resources/dc_flat.tif";
	private static final short BYTE_ORDER = 0x4949;
	private static final short FILE_INDENTIFY = 0x002A;
	private static final short OFFSET = 0x00000008;
	private static final int WIDTH = 1998;
	private static final int HEIGHT = 1080;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.tiffHeader = new TiffHeader(BYTE_ORDER, FILE_INDENTIFY, OFFSET, "asdf.tif");
//		for (int i = 0; i < 13; i++) {
			ImageFileDirectory ifd1 = new ImageFileDirectory();	
			ifd1.addDirectoryEntry(new DirectoryEntry(254, 4, 1, 0));
			ifd1.addDirectoryEntry(new DirectoryEntry(256, 3, 1, WIDTH));
			ifd1.addDirectoryEntry(new DirectoryEntry(257, 3, 1, HEIGHT));
			ifd1.addDirectoryEntry(new DirectoryEntry(258, 3, 3, 170));
			ifd1.addDirectoryEntry(new DirectoryEntry(259, 3, 1, 1));
			ifd1.addDirectoryEntry(new DirectoryEntry(262, 3, 1, 2));
			ifd1.addDirectoryEntry(new DirectoryEntry(273, 4, 1, 1656));
			ifd1.addDirectoryEntry(new DirectoryEntry(277, 3, 1, 3));
			ifd1.addDirectoryEntry(new DirectoryEntry(278, 3, 1, 1080));
			ifd1.addDirectoryEntry(new DirectoryEntry(279, 4, 1, 12947040));
			ifd1.addDirectoryEntry(new DirectoryEntry(284, 3, 1, 1));
			ifd1.addDirectoryEntry(new DirectoryEntry(34377, 1, 24, 176));
			ifd1.addDirectoryEntry(new DirectoryEntry(34675, 7, 1456, 200));
			tiffHeader.addImgFileDirectories(ifd1);
//		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.tiffHeader = null;
	}

	/**
	 * Test method for {@link com.intdm.dc.qc.DimensionsOfTiff#checkTiffHeader(java.lang.String, long, long, long)}.
	 */
	@Test
	public void testCheckTiffHeader() throws Exception{
		assertEquals(DimensionsOfTiff.checkTiffHeader(getClass().getResource(FILE_NAME).getPath(), 1998, 1080, 12948696).getImgFileDirectories().get(0).getDirectoryEntries().get(0).getCount(),  1);
		for (int i = 0; i < DimensionsOfTiff.checkTiffHeader(getClass().getResource(FILE_NAME).getPath(), 1998, 1080, 12948696).getImgFileDirectories().get(0).getDirectoryEntries().get(0).getCount(); i++) {
			assertEquals(DimensionsOfTiff.checkTiffHeader(getClass().getResource(FILE_NAME).getPath(), 1998, 1080, 12948696).getImgFileDirectories().get(0).getDirectoryEntries().get(0).getOffset(),  tiffHeader.getImgFileDirectories().get(0).getDirectoryEntries().get(0).getOffset());
			assertEquals(DimensionsOfTiff.checkTiffHeader(getClass().getResource(FILE_NAME).getPath(), 1998, 1080, 12948696).getImgFileDirectories().get(0).getDirectoryEntries().get(0).getTag(),  tiffHeader.getImgFileDirectories().get(0).getDirectoryEntries().get(0).getTag());
			assertEquals(DimensionsOfTiff.checkTiffHeader(getClass().getResource(FILE_NAME).getPath(), 1998, 1080, 12948696).getImgFileDirectories().get(0).getDirectoryEntries().get(0).getType(),  tiffHeader.getImgFileDirectories().get(0).getDirectoryEntries().get(0).getType());
		}
	}
	
	
	@Test
	public void testInvalidSizeError() {
		try {
			DimensionsOfTiff.checkTiffHeader(getClass().getResource(FILE_NAME).getPath(), 1998, 1080, 12948690);
			fail("Expected an InvalidTiffHeaderExeption to be thrown");
		} catch (InvalidTiffHeaderExeption e) {
//			assertThat(e.getMessage(), is("Invalid image size for " + tiffHeader.getFilePath() + "- " + 12948690));
			System.out.println(e.getMessage());
		} catch (IOException e) {
			fail("Expected an InvalidTiffHeaderExeption to be thrown insted of IOException");
		}
		
	}
	
	@Test
	public void testInvalidWidthError() {
		try {
			DimensionsOfTiff.checkTiffHeader(getClass().getResource(FILE_NAME).getPath(), 1996, 1080, 12948696);
			fail("Expected an InvalidTiffHeaderExeption to be thrown");
		} catch (InvalidTiffHeaderExeption e) {
//			assertThat(e.getMessage(), is("Invalid image width for " + tiffHeader.getFilePath()));
			System.out.println(e.getMessage());
		} catch (IOException e) {
			fail("Expected an InvalidTiffHeaderExeption to be thrown insted of IOException");
		}
		
	}
	
	@Test
	public void testInvalidHeightError() {
		try {
			DimensionsOfTiff.checkTiffHeader(getClass().getResource(FILE_NAME).getPath(), 1998, 1082, 12948696);
			fail("Expected an InvalidTiffHeaderExeption to be thrown");
		} catch (InvalidTiffHeaderExeption e) {
//			assertThat(e.getMessage(), is("Invalid image height for " + tiffHeader.getFilePath()));
			System.out.println(e.getMessage());
		} catch (IOException e) {
			fail("Expected an InvalidTiffHeaderExeption to be thrown insted of IOException");
		}
		
	}
	
	@Test
	public void testInvalidByteOrderError() {
		try {
			DimensionsOfTiff.checkTiffHeader(getClass().getResource("../../../intdm/dc/resources/dc_flat_1.tif").getPath(), 1998, 1080, 6475176);
			fail("Expected an InvalidTiffHeaderExeption to be thrown");
		} catch (InvalidTiffHeaderExeption e) {
			System.out.println(e.getMessage());
//			assertThat(e.getMessage(), is("Invalid byte order!"));
		} catch (IOException e) {
			fail("Expected an InvalidTiffHeaderExeption to be thrown insted of IOException");
		}
		
		
	}

}
