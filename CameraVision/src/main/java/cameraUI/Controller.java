package cameraUI;

public interface Controller extends Runnable{
	
	ControllerData getLabelData();
	
	void startThread();
	
	void stopThread();
	
	void pauseThread();
	
	void unpauseThread();
}
