package com.intdm.dc.qc;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

/**
 * @author Tihomir Monov
 *
 */
@SuppressWarnings("serial")
public class TiffCheck extends JFrame {

	public static final String DEFAULT_INITIAL_DIR = "C:\\WINDOWS\\SYSTEM32";
	public static final String FIND_BUTTON_CAPTION = "Check";
	public static final String STOP_BUTTON_CAPTION = "Stop";
	public static final String STATUS_MESSAGE_READY = "Ready.";

	JTextField fileNameTextField = new JTextField();
	JTextField startDirectoryTextField = new JTextField();
	JTextArea textAreaResult = new JTextArea();
	JButton buttonFindStartStop = new JButton(FIND_BUTTON_CAPTION);
	JLabel statusBarLabel = new JLabel(STATUS_MESSAGE_READY);

	private boolean searchingActive = false;
	private FinderThread finderThread;

	public TiffCheck() {
		this.setTitle("Tiff Sequence Checker");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());

		JPanel searchDetailsPanel = new JPanel();
		searchDetailsPanel.setLayout(new GridLayout(2, 2));

		JLabel labelFileName = new JLabel("Enter res. & size (in bytes) to check (i.e. 1998 1080 12948696):");
		searchDetailsPanel.add(labelFileName);
		searchDetailsPanel.add(fileNameTextField);

		JLabel labelStartDirectory = new JLabel("Enter start directory:");
		searchDetailsPanel.add(labelStartDirectory);
		startDirectoryTextField.setText(DEFAULT_INITIAL_DIR);
		searchDetailsPanel.add(startDirectoryTextField);

		topPanel.add(searchDetailsPanel, BorderLayout.NORTH);

		buttonFindStartStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonFindStartStopPressed();
			}
		});
		JRootPane rootPane = this.getRootPane();
		rootPane.setDefaultButton(buttonFindStartStop);
		topPanel.add(buttonFindStartStop, BorderLayout.SOUTH);

		this.setLayout(new BorderLayout());
		this.add(topPanel, BorderLayout.NORTH);

		textAreaResult.setEditable(false);
		JScrollPane textAreaScrollPane = new JScrollPane(textAreaResult);
		this.add(textAreaScrollPane, BorderLayout.CENTER);

		JPanel statusBar = new JPanel();
		statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusBar.setLayout(new GridLayout());
		statusBar.add(statusBarLabel);
		this.add(statusBar, BorderLayout.SOUTH);

		this.setSize(new Dimension(600, 400));
		this.setVisible(true);
	}

	private void buttonFindStartStopPressed() {
		if (!searchingActive) {
			startSearch();
		} else {
			finderThread.interrupt();
		}
	}

	private void startSearch() {
		searchingActive = true;
		buttonFindStartStop.setText(STOP_BUTTON_CAPTION);
		textAreaResult.setText("Check started.\n");
		finderThread = new FinderThread(this);
		finderThread.start();

		// Try also run() instead of start() to see the difference
		// finderThread.run();
	}

	void searchFinished() {
		searchingActive = false;
		if (finderThread.wasInterrupted()) {
			textAreaResult.append("Check aborted.");
		} else {
			textAreaResult.append("Check finished.");
		}
		statusBarLabel.setText(TiffCheck.STATUS_MESSAGE_READY);
		buttonFindStartStop.setText(FIND_BUTTON_CAPTION);
	}

	public static void main(String[] args) {
		TiffCheck fileFinderFrame = new TiffCheck();
		fileFinderFrame.setVisible(true);
	}

}

class FinderThread extends Thread {

	TiffCheck fileFinderFrame;
	private boolean wasInterrupted;

	public FinderThread(TiffCheck fileFinderFrame) {
		this.fileFinderFrame = fileFinderFrame;
	}

	public void run() {
		String startDirectory = fileFinderFrame.startDirectoryTextField.getText();
		String resolutionToCheck = fileFinderFrame.fileNameTextField.getText();
		wasInterrupted = false;

		find(startDirectory, resolutionToCheck);
		searchFinished();
	}

	private void find(String startDir, String resolutionToCheck) {
//		ArrayList<TiffHeader> tiffHeaders = new ArrayList<TiffHeader>();
		
		if (isValidResolution(resolutionToCheck)) {
			String sWidth = resolutionToCheck.substring(0, resolutionToCheck.indexOf(" "));
			String sHeight = resolutionToCheck.substring(resolutionToCheck.indexOf(" ") + 1, resolutionToCheck.lastIndexOf(" "));
			String sSize = resolutionToCheck.substring(resolutionToCheck.lastIndexOf(" ") + 1);
			Long lWidth = new Long(sWidth);
			Long lHeight = new Long(sHeight);
			Long lSize = new Long(sSize);

			Queue<File> directories = new LinkedList<File>();
			File file = new File(startDir);
			int count = 0;
			int numFiles = 0;
			directories.offer(file);
			while (directories.size() != 0) {
				File currentDirectory = directories.poll();
				File[] tiffFiles = currentDirectory.listFiles(new FileFilter() {
					public boolean accept(File pathname) {
						boolean result = Pattern.matches("\\S+\\.tif$", pathname
								.getPath());
						return result;
					}
				});
				for (File f : tiffFiles) {
					if (this.isInterrupted()) {
						wasInterrupted = true;
						return;
					}
					displayStatus("Checking: " + f.getName());
					if (f.isFile()) {
						try {
							numFiles++;
							displayText(DimensionsOfTiff.checkTiffHeader(f.getAbsolutePath(), lWidth, lHeight, lSize).toString());
							count ++;
						} catch (IOException e) {
							displayText("ERROR! (" + e.getMessage() + ") ");
							return;
						} catch (InvalidTiffHeaderExeption e) {
							displayText("ERROR! (" + e.getMessage() + ") ");
						}
					}
				}
				File[] subdirectories = currentDirectory.listFiles();
				for (File f : subdirectories) {
					if (f.isDirectory()) {
						directories.offer(f);
					}
				}
			}
			displayText(count + " headers from " + numFiles +" match with resolution " + sWidth + "px " + sHeight + "px" + " and size " + sSize);
		} else {
			displayText("Invalid format for the resolution!\nValid formats for example are \"1998 1080 12948696\" or \"2048 858 12948696\"");
		}
	}

	private boolean isValidResolution(String s) {
		String patternIntegerNumber = "(0|[+-]?[1-9][0-9]*)\\s(0|[+-]?[1-9][0-9]*)\\s[0-9]*[0-9]+$";
		boolean valid = s.matches(patternIntegerNumber);
		return valid;
	}

	private void displayText(final String message) {
		final JTextArea textAreaResult = this.fileFinderFrame.textAreaResult;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				textAreaResult.append(message + "\n");
			}
		});
	}

	private void displayStatus(final String message) {
		final JLabel statusBarLabel = this.fileFinderFrame.statusBarLabel;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				statusBarLabel.setText(message);
			}
		});
	}

	private void searchFinished() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				fileFinderFrame.searchFinished();
			}
		});
	}

	public boolean wasInterrupted() {
		return wasInterrupted;
	}

}
