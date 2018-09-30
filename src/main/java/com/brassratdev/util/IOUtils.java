package com.brassratdev.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class IOUtils {
	
	public static ByteBuffer cloneAsByteBuffer(InputStream is) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int readLength = 0;
			while ((readLength = is.read(buffer)) != -1) {
				outputStream.write(buffer, 0, readLength);
			}
			outputStream.flush();
			return ByteBuffer.wrap(outputStream.toByteArray());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public static ByteArrayOutputStream toByteArrayOutputStream(InputStream is) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int readLength = 0;
			while ((readLength = is.read(buffer)) != -1) {
				outputStream.write(buffer, 0, readLength);
			}
			outputStream.flush();
			return outputStream;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
