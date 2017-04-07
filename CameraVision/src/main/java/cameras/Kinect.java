package cameras;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;

import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.ufl.digitalworlds.j4k.J4KSDK;
import edu.ufl.digitalworlds.j4k.VideoFrame;


/*
 * Copyright 2011-2014, Digital Worlds Institute, University of 
 * Florida, Angelos Barmpoutis.
 * All rights reserved.
 *
 * When this program is used for academic or research purposes, 
 * please cite the following article that introduced this Java library: 
 * 
 * A. Barmpoutis. "Tensor Body: Real-time Reconstruction of the Human Body 
 * and Avatar Synthesis from RGB-D', IEEE Transactions on Cybernetics, 
 * October 2013, Vol. 43(5), Pages: 1347-1356. 
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *     * Redistributions of source code must retain this copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce this
 * copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
public class Kinect extends J4KSDK implements Camera{
	private static final double updateInterval = 2.2;

	private ViewerPanel3D viewer=null;
	JLabel label=null;
	boolean mask_players=false;

	private capturedImage lastImage;

	private double timer;
	private double elapsed;
	private int frameCount;
	private double FPS;

	public Kinect()
	{
		lastImage = new capturedImage();
		timer = System.currentTimeMillis();
		elapsed = 0.0;
		frameCount = 0;
		FPS = 0.0;
		
		initialize();
	}

	public Kinect(byte type)
	{
		super(type);
	}

	public void initialize() {
		this.setDepthResolution(320, 240);
		this.setColorResolution(640, 480);

		if(!this.start(J4KSDK.COLOR))
		{
			System.out.println("ERROR: The Kinect device could not be initialized.\n\n 1. Check if the Microsoft's Kinect SDK was succesfully installed on this computer.\n 2. Check if the Kinect is plugged into a power outlet.\n  3. Check if the Kinect is connected to a USB port of this computer.");
			//System.exit(0); 
		} else System.out.println("Kinect Started");
		this.viewer = new ViewerPanel3D();
		this.viewer.setShowVideo(false);
		this.viewer.setDrawFlatTexture(true);
	}

	public void maskPlayers(boolean flag) {
		mask_players=flag;
	}

	public void close() {
		this.stop();
	}

	public void setViewer(ViewerPanel3D viewer){this.viewer=viewer;}

	public void setLabel(JLabel l){this.label=l;}

	private boolean use_infrared=false;

	public void updateTextureUsingInfrared(boolean flag)
	{
		use_infrared=flag;
	}

	public VideoFrame getVideoTexture() {
		return viewer.getVideoFrame();
	}

	@Override
	public void onColorFrameEvent(byte[] data) {
		if(viewer==null || viewer.videoTexture==null || use_infrared) {
			return;
		}
		viewer.videoTexture.update(getColorWidth(), getColorHeight(), data);

		elapsed += (System.currentTimeMillis() -timer);
		timer = System.currentTimeMillis();
		frameCount++;

		if(elapsed > updateInterval*1000.0) {
			FPS = ((double)frameCount) / (elapsed/1000.0);
			elapsed %= updateInterval*1000.0;
			lastImage.setCameraCapture(createBufferedImageFromData(data));
		}
	}


	public JPanel getPanel() {

		return (JPanel)this.viewer;
	}

	private BufferedImage createBufferedImageFromData(byte[] imageData) {
		//byte[] imageData = getVideoTexture().getData();
		//System.out.println("Size of captured imageData: " + imageData.length);

		byte[] RGB = new byte[getColorWidth()* getColorHeight()*3];

		// OpenGL uses:  GL2.GL_BGRA, create RGB byte array
		int j=0;
		for (int i = 0;  i < imageData.length; i += 4) {
			byte b, g, r;

			b = imageData[i];
			g = imageData[i + 1];
			r = imageData[i + 2];

			RGB[j++] = b; 
			RGB[j++] = g; 
			RGB[j++] = r;
		}

		BufferedImage image = new BufferedImage(getColorWidth(), getColorHeight(), BufferedImage.TYPE_3BYTE_BGR);
		image.setData(Raster.createRaster(image.getSampleModel(), new DataBufferByte(RGB, RGB.length), new Point() ));

		return image;
	}

	@Override
	public void onDepthFrameEvent(short[] arg0, byte[] arg1, float[] arg2, float[] arg3) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSkeletonFrameEvent(boolean[] arg0, float[] arg1, float[] arg2, byte[] arg3) {
		// TODO Auto-generated method stub
	}

	@Override
	public capturedImage getCapturedImage() {
		return this.lastImage;
	}

}
