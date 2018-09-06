package com.brassratdev.media;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.media.ConfigureCompleteEvent;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.DataSink;
import javax.media.EndOfMediaEvent;
import javax.media.Format;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.PrefetchCompleteEvent;
import javax.media.Processor;
import javax.media.RealizeCompleteEvent;
import javax.media.ResourceUnavailableEvent;
import javax.media.control.TrackControl;
import javax.media.datasink.DataSinkErrorEvent;
import javax.media.datasink.DataSinkEvent;
import javax.media.datasink.DataSinkListener;
import javax.media.datasink.EndOfStreamEvent;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.protocol.FileTypeDescriptor;

/**
 * <p>
 * Title: MovieMaker.java
 * </p>
 * <p>
 * Description: This class will, given an input of files, write them to a movie
 * file for presentation by the MovieViewer.java
 * </p>
 * <p>
 * Copyright: Copyright (c) Robert Damus 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class MovieMaker implements DataSinkListener, ControllerListener {
	private Processor processor;
	private Object waitSync = new Object();
	private Object waitFileSync = new Object();
	private boolean stateTransitionOK = true;
	private boolean compressionRequired = false;
	private boolean debug = false;
	private boolean fileDone = false;
	private boolean fileSuccess = true;
	private MediaLocator outputMediaLocator;
	private ImageBufferDataSource dataSource;
	private DataSink outputDataSink;

	public MovieMaker(Vector<String> images, float frameRate, String filePrefix) {
		this(new ImageBufferDataSource(images, frameRate), filePrefix);
	}

	public MovieMaker(ImageBufferDataSource dataSource, String filePrefix) {
		// for now, we will make quicktime compatible movies
		this.outputMediaLocator = createMediaLocator(filePrefix + ".mov");

		if (initDataSource(dataSource))
			processImages();
		else
			System.err.println("MovieMaker(): failed to initMovieMaker");

	}

	private boolean processImages() {
		try {
			// make the processor with the datasource of images we rx'd
			processor = Manager.createProcessor(dataSource);
		} catch (Exception e) {
			if (debug) {
				System.err
						.println("MovieMaker: open(): "
								+ "Failed to create a processor from the given dataSource: "
								+ e);
			}
			return false;
		}

		processor.addControllerListener(this);

		// Put the Processor into configured state.
		processor.configure();

		if (!waitForState(processor.Configured)) {
			if (debug) {
				System.err.println("MovieMaker: open(): "
						+ "Failed to configure the processor.");
			}
			return false;
		}

		// Set the output content descriptor to QuickTime.
		processor.setContentDescriptor(new ContentDescriptor(
				FileTypeDescriptor.QUICKTIME));
		// processor.setContentDescriptor(new
		// ContentDescriptor(FileTypeDescriptor.MSVIDEO));

		// Obtain controls registered for this player
		TrackControl controls[] = processor.getTrackControls();
		// only getting a single track feed
		Format formats[] = controls[0].getSupportedFormats();
		if (formats == null || formats.length <= 0) {
			System.err.println("The mux does not support the input format: "
					+ controls[0].getFormat());
			return false;
		}

		controls[0].setFormat(formats[0]);

		System.err.println("Setting the track format to: " + formats[0]);

		// Realize the processor.
		// only after realizing can FrameGrabbingControl be implemented
		// processor.prefetch();
		processor.realize();
		// if (!waitForState(processor.Prefetched)) {
		if (!waitForState(processor.Realized)) {
			if (debug) {
				System.err.println("MovieMaker: open(): "
						+ "Failed to realize the processor.");
			}
			return false;
		}

		// the last step is to create the DataSink that we will write the
		// imagebuffer data to
		return createOutputMovieFile();
	}

	private boolean createOutputMovieFile() {
		// Now, we'll need to create a DataSink.
		if (!createDataSink()) {
			System.err.println("Failed to create a DataSink for: "
					+ outputMediaLocator);
			return false;
		}

		outputDataSink.addDataSinkListener(this);

		System.err.println("start processing...");

		// OK, we can now start the actual transcoding.
		try {
			processor.start();
			outputDataSink.start();
		} catch (IOException e) {
			System.err.println("IO error during processing");
			return false;
		}

		// Wait for EndOfStream event.
		waitForFileDone();

		// Cleanup.
		try {
			outputDataSink.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		processor.removeControllerListener(this);

		System.err.println("...done processing.");
		return true;
	}

	private boolean createDataSink() {
		try {
			System.err.println("- create DataSink for: " + outputMediaLocator);
			DataSource ds = processor.getDataOutput();
			System.err.println("- DataSource: " + dataSource.toString());
			System.err.println("- ds: " + ds.toString());
			outputDataSink = Manager.createDataSink(ds, outputMediaLocator);
			outputDataSink.open();

			return true;
		} catch (Exception e) {
			System.err.println("Cannot create the DataSink: " + e);

			return false;
		}
	}

	/**
	 * Block until file writing is done.
	 */
	boolean waitForFileDone() {
		synchronized (waitFileSync) {
			try {
				while (!fileDone)
					waitFileSync.wait();
			} catch (Exception e) {
			}
		}
		return fileSuccess;
	}

	private boolean initDataSource(ImageBufferDataSource dataSource) {
		this.dataSource = dataSource;

		System.err.println("initMovieMaker(): DataSource: "
				+ dataSource.toString());
		System.err.println("initMovieMaker(): DataSource class: "
				+ dataSource.getClass().toString());
		try {
			dataSource.connect();
			return true;
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}

	}

	/**
	 * DataSinkListener
	 * 
	 * @param evt
	 *            an event posted by the writing to the outputMediaLocator
	 */
	public void dataSinkUpdate(DataSinkEvent evt) {
		if (evt instanceof EndOfStreamEvent) {
			synchronized (waitFileSync) {
				fileDone = true;
				waitFileSync.notifyAll();
			}
		} else if (evt instanceof DataSinkErrorEvent) {
			synchronized (waitFileSync) {
				fileDone = true;
				fileSuccess = false;
				waitFileSync.notifyAll();
			}
		}

	}

	/**
	 * Block until the processor has transitioned to the given state. Return
	 * false if the transition failed.
	 */
	boolean waitForState(int state) {
		synchronized (waitSync) {
			try {
				while (processor.getState() != state && stateTransitionOK)
					waitSync.wait();
			} catch (Exception e) {
			}
		}
		return stateTransitionOK;
	}

	/**
	 * Controller Listener.
	 */
	public void controllerUpdate(ControllerEvent evt) {

		if (evt instanceof ConfigureCompleteEvent
				|| evt instanceof RealizeCompleteEvent
				|| evt instanceof PrefetchCompleteEvent) {

			synchronized (waitSync) {
				stateTransitionOK = true;
				waitSync.notifyAll();
			}
		} else if (evt instanceof ResourceUnavailableEvent) {
			synchronized (waitSync) {
				stateTransitionOK = false;
				waitSync.notifyAll();
			}
		} else if (evt instanceof EndOfMediaEvent) {
			evt.getSourceController().stop();
			evt.getSourceController().close();
		}
	}

	/**
	 * Create a media locator from the given string.
	 */
	static MediaLocator createMediaLocator(String url) {

		MediaLocator ml;

		if (url.indexOf(":") > 0 && (ml = new MediaLocator(url)) != null)
			return ml;

		if (url.startsWith(File.separator)) {
			if ((ml = new MediaLocator("file:" + url)) != null)
				return ml;
		} else {
			String file = "file:" + System.getProperty("user.dir")
					+ File.separator + url;
			if ((ml = new MediaLocator(file)) != null)
				return ml;
		}

		return null;
	}

}