package visionPatterns;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import computervision.GregggResNet;
import computervision.ImageLabel;
import computervision.ImageProcessor;
import computervision.QRReader;
import computervision.ResNet50;

public class ThreadedClassifier implements ThreadedCameraProcessor {
	private ImageProcessor myImgProcessor;
	
	private ArrayList<ImageLabelObserver> myObservers;
	private ImageLabel result;

	private BufferedImage readNext;
	private volatile boolean readYet;
	private volatile boolean imageProcessing;
	private volatile boolean threadRunning;
	private Thread imageProcessingThread; 
	//mutex?

	public ThreadedClassifier() {
		myObservers = new ArrayList<ImageLabelObserver>();
		imageProcessingThread = new Thread(this);
		result = null;
		
		//myImgProcessor = new ResNet50();    //imagenet trained
		myImgProcessor = new GregggResNet();
		//myImgProcessor = new QRReader();

		readNext = null;
		readYet = true;
		imageProcessing = false;
		threadRunning = false;
	}

	public void registerLabelObserver(ImageLabelObserver newObserver) {
		myObservers.add(newObserver);
	}


	private synchronized boolean isReadYet() {
		return readYet;
	}

	private synchronized void setReadYet(boolean readYet) {
		this.readYet = readYet;
	}

	public synchronized void updateBufferedImage(BufferedImage newImage) {
		if(isReadYet()) {
			System.out.println("New image recvd");
			readNext = newImage;
			setReadYet(false);
		}
	}
	
	private void resultsChanged() {
		notifyObservers();
	}
	
	private void notifyObservers() {
		for(ImageLabelObserver singleObserver: myObservers) {
			singleObserver.updatePredictions(result);
		}
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
				result = this.myImgProcessor.scanImage(readNext);
				setReadYet(true);
				elapsed = System.currentTimeMillis()-start;
				System.out.println("Prediction took " + elapsed + "milliseconds");
				resultsChanged();
			}
		}


	}

}
