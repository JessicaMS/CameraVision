package visionPatterns;

import cameras.CameraObserver;

public interface ThreadedCameraProcessor extends Runnable, CameraObserver {
	
	public boolean isImageProcessing();

	public void setImageProcessing(boolean runClassifier);
	
	public void startThread();
	
	public void stopThread();
}
