package com.brassratdev.media;

import javax.media.Buffer;
import java.io.IOException;
import java.io.FileNotFoundException;
import javax.media.Format;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.PullBufferStream;
import java.util.Vector;
import java.awt.Dimension;
import javax.media.format.RGBFormat;
import javax.media.Control;
import javax.media.format.VideoFormat;
import java.io.RandomAccessFile;
import java.io.InputStream;
import java.io.FileInputStream;
import javax.media.jai.RenderedOp;
import javax.media.jai.JAI;
import com.sun.media.jai.codec.SeekableStream;
import java.awt.image.renderable.ParameterBlock;
import javax.media.jai.PlanarImage;
import java.awt.image.BufferedImage;
import javax.media.util.ImageToBuffer;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.ImageCodec;
import java.io.FileOutputStream;
import com.sun.media.jai.codec.JPEGEncodeParam;
import java.util.TreeMap;

/**
 * <p>Title: ImageBufferStream</p>
 * <p>Description: This class takes .bmp frame captures, encodes them as a .jpg using JAI tools,
 * and then exports them via the read(Bufffer buf) interface degfined in PullBufferStream</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author rdamus
 * @version 1.0
 */

public class ImageBufferStream
    implements PullBufferStream {
    protected ContentDescriptor cd = new ContentDescriptor(ContentDescriptor.
        RAW);
    protected int maxDataLength;
    protected byte[] dataToRender;
    protected Dimension size;
    protected VideoFormat videoFormat;
    protected Control[] controls = new Control[0];
    protected int seqNo = 0;
    protected float frameRate = 1f;//15f;//
    protected int bufferCap = 15;
    protected Vector images;
    protected int width = 1280, height = 960;//640, height = 480; //hard coded for Sensoray FG

    protected boolean started = false;
    protected boolean ended = false;

    public ImageBufferStream(Vector images, float frameRate) {
        this.images = images;
        this.frameRate = frameRate;

        this.videoFormat = new VideoFormat(VideoFormat.JPEG,
                                      new Dimension(width, height),
                                      Format.NOT_SPECIFIED,
                                      Format.byteArray,
                                      frameRate);
        this.sortImages();
    }

    /**
     * A TreeMap isA SortedMap, and will be used to sort on the
     * frameNumber that is appended to each frame image
     */
    private void sortImages(){
        Object[] fileNames = images.toArray();
        TreeMap extToName = new TreeMap();
        for(int i = 0; i < fileNames.length; i++){
            String name = (String)fileNames[i];
            int idx = name.indexOf("_");
            String after_ = name.substring(idx+1, name.length());
            int dot = after_.indexOf(".");
            System.out.println("pruning: " + name);
            Integer no = new Integer(after_.substring(0, dot));
            extToName.put(no,name);
        }
        //create the new Vector of sorted image names
        images = new Vector(extToName.values());
    }


    public boolean willReadBlock() {
        return false;
    }

    public void read(Buffer buf) throws java.io.IOException {
        // Check if we've finished all the frames.
        if (seqNo >= images.size()) {
            // We are done.  Set EndOfMedia.
            System.err.println("Done reading all images.");
            buf.setEOM(true);
            buf.setOffset(0);
            buf.setLength(0);
            ended = true;
            return;
        }

        String imageFile = (String) images.elementAt(seqNo);
        seqNo++;

        System.err.println("  - reading bmp file: " + imageFile);

        // Wrap the InputStream in a SeekableStream.
        InputStream is = new FileInputStream(imageFile);
        SeekableStream s = SeekableStream.wrapInputStream(is, false);

        // Create the ParameterBlock and add the SeekableStream to it.
        ParameterBlock pb = new ParameterBlock();
        pb.add(s);


        // Perform the BMP operation
        //then encode the BMP as a JPEG
        try {
            PlanarImage planarImage = JAI.create("BMP", pb);
            JPEGEncodeParam encodeParam = null;
            FileOutputStream out = new FileOutputStream("tmp.jpg");
            ImageEncoder encoder = ImageCodec.createImageEncoder("JPEG", out, encodeParam);

            encoder.encode(planarImage);
            out.close();
        } catch (IOException e) {
            System.out.println("IOException at encoding..");
            System.exit(1);
        }

        //BMP extraction
//        PlanarImage planarImage = JAI.create("JPEG", pb);
//        BufferedImage buffImage = planarImage.getAsBufferedImage();
        //now grab the buffer
//        ImageToBuffer imageToBuffer = new ImageToBuffer();
//        Buffer bmpImageBuffer = imageToBuffer.createBuffer(buffImage, frameRate);

        // Open a random access file for the next image.
        RandomAccessFile raFile = null;
        try{
            raFile = new RandomAccessFile("tmp.jpg", "r");
        }catch(FileNotFoundException fnfe){
            fnfe.printStackTrace();
        }

        byte data[] = null;

        // Check the input buffer type & size.

        if (buf.getData()instanceof byte[]) {
            data = (byte[]) buf.getData();
        }

//            // Check to see the given buffer is big enough for the frame.
        if (data == null || data.length < raFile.length()) {
//        if (data == null || data.length < bmpImageBuffer.getLength()) {
//            data = new byte[ (int) bmpImageBuffer.getLength()];
            data = new byte[ (int) raFile.length() ];
            buf.setData(data);
        }

        // Read the entire JPEG image from the file.
        raFile.readFully(data, 0, (int) raFile.length());
        System.err.println("    read " + raFile.length() + " bytes.");
        buf.setLength( (int) raFile.length());

        //copy the array over
//        if( bmpImageBuffer.getData() instanceof int[] ){
//            int bmpData[] = (int[])bmpImageBuffer.getData();
//            for(int i = 0; i < bmpData.length; i++){
//                data[i] = (byte)bmpData[i];
//            }//another way?  seems ugly
//        }else if (bmpImageBuffer.getData() instanceof int[] ){
//            byte[] bmpData = (byte[])bmpImageBuffer.getData();
//            System.arraycopy( bmpData, 0, data, 0, bmpImageBuffer.getLength());
//        }else
//            System.err.println("bmpImageBuffer unknown type");
//
//        buf.setData(bmpImageBuffer.getData());
//        System.err.println("    copied " + bmpImageBuffer.getLength() + " bytes.");
//        buf.setLength( (int) bmpImageBuffer.getLength() );

        //finish off the buffer
        buf.setOffset(0);
        buf.setFormat(videoFormat);
        buf.setFlags(buf.getFlags() | buf.FLAG_KEY_FRAME);

        // Close the random access file.
        raFile.close();

    }

    public Format getFormat() {
        return this.videoFormat;
    }

    public ContentDescriptor getContentDescriptor() {
        return this.cd;
    }

    public long getContentLength() {
        return 0;
    }

    public boolean endOfStream() {
        return ended;
    }

    public Object[] getControls() {
        return this.controls;
    }

    public Object getControl(String parm1) {
        return null;
    }

}