package com.brassratdev.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;



/**
 * this abstract class wraps a FileChannel and maps the file into memory using
 * the FileChannel.map method. Data is subsequently read from the buffer in LSB
 * format (Little Endian)
 * 
 * @author rdamus
 * 
 */
public abstract class AbstractFileChannel {

	protected FileChannel fileChannel;
	protected ByteBuffer fileBytes;
	protected File file;
	protected ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
	protected boolean output;

	public AbstractFileChannel(File file) {
		this.file = file;
		this.output = false;
	}

	public AbstractFileChannel(String file, boolean bOutput) {
		this(new File(file));
		this.output = bOutput;
	}

	/**
	 * read into the byte[] array.  assert buff has a positive length, then called ByteBuffer.get() method to extract
	 *  the data from the underlying ByteBuffer fileBytes.
	 * @param buff the byte[] array we read in to
	 * @return the length of the buffer returned
	 */
	public int read(byte[] buff) {
		assert buff.length > 0;

		getFileBytes().get(buff);

		return buff.length;
	}
	
	/**
	 * a faster read version that slice()'s the underlying ByteBuffer fileBytes.
	 * @param buff a ByteBuffer to hold the data
	 * @return the capacity() of the buffer created for storing the data
	 */
	public long read(ByteBuffer buff){
		buff = getFileBytes().slice();
		return getFileBytes().capacity();
	}

	/**
	 * write buff to the fileChannel.  calls FileChannel.write and wraps the byte[] buffer 
	 * @param buff the byte[] array to be written to the fileChannel
	 * @return the number of bytes written to the fileChannel
	 * @throws IOException
	 */
	public int write(byte[] buff) throws IOException {
		assert buff.length > 0;

		return fileChannel.write(ByteBuffer.wrap(buff));
	}
	
	/**
	 * write buff to the fileChannel, using java.nio methods.
	 * @param buff thr source of bytes to be written
	 * @return the number of bytes written
	 * @throws IOException
	 */
	public int write(ByteBuffer buff) throws IOException {
		return fileChannel.write( buff );
	}

	protected abstract long size() throws IOException;

	public void open() throws Exception {
		if (output) {
			fileChannel = new FileOutputStream(file).getChannel();
			fileBytes = fileChannel.map(MapMode.READ_WRITE, fileChannel
					.position(), size());
		} else {
			fileChannel = new FileInputStream(file).getChannel();
			// map into memory
			fileBytes = fileChannel.map(MapMode.READ_ONLY, fileChannel
					.position(), fileChannel.size());
		}
		fileBytes.order(byteOrder);
	}

	public void close() throws IOException {
		fileChannel.close();
		// clear the buffer
		getFileBytes().clear();
	}

	public FileChannel getFileChannel() {
		return fileChannel;
	}

	public void setFileChannel(FileChannel fileChannel) {
		this.fileChannel = fileChannel;
	}

	public ByteBuffer getFileBytes() {
		return fileBytes;
	}

	public void setFileBytes(ByteBuffer fileBytes) {
		this.fileBytes = fileBytes;
	}

	public ByteOrder getByteOrder() {
		return byteOrder;
	}

	public void setByteOrder(ByteOrder byteOrder) {
		this.byteOrder = byteOrder;
	}

}
