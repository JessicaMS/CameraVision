package computervision;

import cameras.CameraObserver;

public interface ImageProcessor extends Runnable, CameraObserver {
	
	public boolean isImageProcessing();

	public void setImageProcessing(boolean runClassifier);
	
	public void stopThread();
}
