package com.brassratdev.media;

import java.io.IOException;
import java.util.Vector;

import javax.media.Time;
import javax.media.protocol.PullBufferDataSource;
import javax.media.protocol.PullBufferStream;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author rdamus
 * @version 1.0
 */

public class ImageBufferDataSource extends PullBufferDataSource {
	protected Object[] controls = new Object[0];
	protected boolean started = false;
	protected String contentType = "raw";
	protected boolean connected = false;
	protected Time duration = DURATION_UNKNOWN;
	protected PullBufferStream[] streams = null;
	protected PullBufferStream stream = null;

	public ImageBufferDataSource(Vector images, float frameRate) {
		this.stream = new ImageBufferStream(images, frameRate);
	}

	public ImageBufferDataSource(PullBufferStream stream) {
		this.stream = stream;
	}

	public void stop() throws java.io.IOException {
		if ((!connected) || (!started))
			return;

		this.started = false;
	}

	public Time getDuration() {
		return this.duration;
	}

	public Object getControl(String controlType) {
		try {
			Class cls = Class.forName(controlType);
			Object cs[] = getControls();
			for (int i = 0; i < cs.length; i++) {
				if (cls.isInstance(cs[i]))
					return cs[i];
			}
			return null;

		} catch (Exception e) { // no such controlType or such control
			return null;
		}

	}

	public void connect() throws java.io.IOException {
		if (connected)
			return;

		this.connected = true;

	}

	public Object[] getControls() {
		return this.controls;
	}

	public void start() throws java.io.IOException {
		// we need to throw error if connect() has not been called
		if (!connected)
			throw new java.lang.Error(
					"ImageDataSource must be connected before it can be started");

		if (started)
			return;

		this.started = true;
	}

	public PullBufferStream[] getStreams() {
		if (streams == null) {
			streams = new PullBufferStream[1];
			streams[0] = this.stream;
		}

		return streams;

	}

	public String getContentType() {
		if (!connected) {
			System.err.println("Error: ImageDataSource not connected");
			return null;
		}

		return this.contentType;

	}

	public void disconnect() {
		try {
			if (started)
				stop();
		} catch (IOException e) {
		}

		this.connected = false;
	}
}