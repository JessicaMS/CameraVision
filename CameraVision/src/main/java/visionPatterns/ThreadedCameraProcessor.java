package visionPatterns;

import computervision.ImageLabel;

public interface ThreadedCameraProcessor extends Runnable {
	
	public ImageLabel getImageLabel();
	
	public boolean isImageProcessing();

	public void setImageProcessing(boolean runClassifier);
	
	public void startThread();
	
	public void stopThread();
}
