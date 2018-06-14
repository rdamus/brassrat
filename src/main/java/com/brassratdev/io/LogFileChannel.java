package com.brassratdev.io;

import java.io.File;
import java.io.IOException;

public class LogFileChannel extends AbstractFileChannel {

	public LogFileChannel(File file) {
		super(file);
	}
	
	public LogFileChannel(String file, boolean bOutput) {
		super(file, bOutput);
	}

	@Override
	protected long size() throws IOException {
		return file.length();
	}

}
