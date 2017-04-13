package visionPatterns;

import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

import cameras.capturedImage;
import computervision.GregggResNet;
import computervision.ImageLabel;
import computervision.ClassificationStrategy;
import computervision.QRReader;
import computervision.ResNet50;

public class ThreadedClassifier implements Observer, ThreadedCameraProcessor {
	private ClassificationStrategy myImgProcessor;
	private ImageLabel resultingLabel;

	private BufferedImage readNext;
	private volatile boolean readYet;
	private volatile boolean imageProcessing;
	private volatile boolean threadRunning;
	private Thread imageProcessingThread; 
	//mutex?

	public ThreadedClassifier(Observable observable) {
		imageProcessingThread = new Thread(this);
		resultingLabel = new ImageLabel();
		observable.addObserver(this);
		
		//myImgProcessor = new ResNet50();    //imagenet trained
		//myImgProcessor = new GregggResNet();
		myImgProcessor = new QRReader();

		readNext = null;
		readYet = true;
		imageProcessing = false;
		threadRunning = false;
	}

	private synchronized boolean isReadYet() {
		return readYet;
	}

	private synchronized void setReadYet(boolean readYet) {
		this.readYet = readYet;
	}


	
	public boolean isImageProcessing() {
		return imageProcessing;
	}

	public void setImageProcessing(boolean runClassifier) {
		this.imageProcessing = runClassifier;
	}
	
	public void startThread() {
		threadRunning = true;
		this.imageProcessingThread.start();
	}
	
	public void stopThread() {
		threadRunning = false;
	}

	public void run() {
		long start, elapsed;
		while(threadRunning) {
			if (imageProcessing == true && !isReadYet()) {
				
				start = System.currentTimeMillis();
				resultingLabel.setLabel(this.myImgProcessor.scanImage(readNext));
				setReadYet(true);
				elapsed = System.currentTimeMillis()-start;
				System.out.println("Prediction took " + elapsed + "milliseconds");

			}
		}


	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof capturedImage) {
			if(isReadYet()) {
				System.out.println("New image recvd");
				readNext = ((capturedImage)o).getCameraCapture();
				setReadYet(false);
			}
		}
		
	}

	@Override
	public ImageLabel getImageLabel() {
		return this.resultingLabel;
	}

}
