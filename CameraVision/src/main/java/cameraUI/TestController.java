package cameraUI;

import javax.swing.JPanel;

import computervision.ImageLabel;
import visionPatterns.CameraProcessor;

public class TestController implements Controller {
	private CameraProcessor cameraProcessor;
	private ControllerData labelData;


	private Thread mainThread;

	private volatile boolean running;
	private volatile boolean paused;
	

	public TestController(CameraProcessor cameraProcessor) {
		this.cameraProcessor = cameraProcessor;
		mainThread = new Thread(this);
		labelData = new ControllerData();

		paused = true;
		running = false;
	}	

	public ControllerData getLabelData() {
		return labelData;
	}
	
	public void startThread() {
		if(running == false) {
			System.out.println("turn on");
			this.running = true;
			mainThread.start();
		}
	}

	public void stopThread() {
		if (running == true) {
			System.out.println("turn off");
			this.running = false;
		}

	}

	public void run() {
		// TODO Auto-generated method stub
		ImageLabel result = null;
		String tourLocData="";
		while(running){
			while(paused) {
				try {
					System.out.println("Thread paused");
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("scan");
			result = cameraProcessor.scanImage();
			if(!result.getLabelName().equals("(None)")) {
				tourLocData=result.getLabelName();
				System.out.println(tourLocData);
				//							tourLocData=IC.requestTLData(tourLocData);
				//							IC.speak(tourLocData);
				result.setLabelName("None)");
				labelData.setLocationName(result.getLabelName());

			} else {
				labelData.setLocationName(result.getLabelName() + "(debug)");
				System.out.println("None, sleep");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

	}

	@Override
	public void pauseThread() {
		this.paused = true;
		
	}

	@Override
	public void unpauseThread() {
		this.paused = false;
		
	}


}
