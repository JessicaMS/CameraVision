package computervision;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import cameras.CameraObserver;

public class SceneClassifier implements Runnable, CameraObserver {
	private ResNet50 myNN; 
	private ArrayList<ClassificationsObserver> myObservers;
	private ArrayList<Classification> results;

	private BufferedImage readNext;
	private boolean readYet;
	private boolean runClassifier;
	private boolean threadRunning;
	//mutex

	public SceneClassifier() {
		myObservers = new ArrayList<ClassificationsObserver>();
		results = new ArrayList<Classification>();
		
		myNN = new ResNet50();
		myNN.loadResNet50();

		readNext = null;
		readYet = true;
		runClassifier = false;
		threadRunning = true;
	}

	public void registerClassificationObserver(ClassificationsObserver newObserver) {
		myObservers.add(newObserver);
	}

	public void updateBufferedImage(BufferedImage newImage) {
		if(readYet == true) {
			System.out.println("New image recvd");
			readNext = newImage;
			readYet = false;
		}

	}
	
	private void resultsChanged() {
		notifyObservers();
	}
	
	private void notifyObservers() {
		for(ClassificationsObserver singleObserver: myObservers) {
			singleObserver.updatePredictions(results);
		}
	}

	public boolean isRunClassifier() {
		return runClassifier;
	}

	public void setRunClassifier(boolean runClassifier) {
		this.runClassifier = runClassifier;
	}
	
	public void stopThread() {
		threadRunning = false;
	}

	public void run() {
		long start, elapsed;
		threadRunning = true;
		while(threadRunning) {
			System.out.print(".");
			if (runClassifier == true && readYet == false) {
				start = System.currentTimeMillis();
				results = this.myNN.doPrediction(readNext);
				readYet = true;
				elapsed = System.currentTimeMillis()-start;
				System.out.println("Prediction took " + elapsed + "milliseconds");
				resultsChanged();
			}
		}


	}

}