package controlPolicies;

public interface ControlPolicy extends Runnable{
	
	ControllerData getLabelData();
	
	void startThread();
	
	void stopThread();
	
	void pauseThread();
	
	void unpauseThread();
}
