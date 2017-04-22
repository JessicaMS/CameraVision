package controlPolicies;

import cameras.Camera;
import computervision.Classifier;
import computervision.GregggResNet;
import computervision.ImageLabel;
import computervision.QRReader;
import computervision.Recorder;
import simpleJoyStick.ButtonData;
import simpleJoyStick.ButtonEcho;
import simpleJoyStick.SimpleJoystick;

import computervision.ResNet50;

import java.util.Observable;
import java.util.Observer;

public class JoystickImageRecorder implements ControlPolicy, Observer  {
	private Classifier cameraClassifier;
	private Recorder myRecorder;
	
	private ControllerData labelData;
	private Camera myCamera;
	private SimpleJoystick joystick;
	private ButtonEcho echo;

	private Thread mainThread;
	private volatile boolean running;
	private volatile boolean paused;


	private boolean zeroPressed = false;
	private boolean onePressed = false;
	private boolean twoPressed = false;
	private boolean threePressed = false;
	private boolean fourPressed = false;
	private boolean upPressed = false;
	private boolean downPressed = false;
	private boolean leftPressed = false;
	private boolean rightPressed = false;



	public JoystickImageRecorder(Camera myCamera) {
		mainThread = new Thread(this);
		labelData = new ControllerData();
		cameraClassifier = new Classifier(new GregggResNet(), myCamera.getCapturedImage());
		myRecorder = new Recorder(myCamera.getCapturedImage());
		joystick = new SimpleJoystick();

		this.myCamera = myCamera;

		paused = true;
		running = false;

		//Not doing anything with this yet.
		//sensorData = new SensorData(hwManager);
		echo = new ButtonEcho();
		if(joystick.getButtonData() != null) {
			joystick.getButtonData().addObserver(this);
			joystick.getButtonData().addObserver(echo);

			joystick.StartThread();	
		}

	}	

	public ControllerData getLabelData() {
		return labelData;
	}

	void controllerUpdated() {
	}

	public void run() {
		ImageLabel result = null;
		String tourLocData="";

		while(running){
			try{Thread.sleep(500);} 
			catch (Exception e){e.printStackTrace();}

			
			if(zeroPressed) {
				zeroPressed = false;
			}
			
			if (onePressed) {
				myRecorder.processImage();
			}

			if (twoPressed) {
				result = cameraClassifier.processImage();
				//if(!result.getLabelName().equals("(None)")) {
					System.out.println("Found: " + result.getLabelName() + result.getProbability());
					//Set the text to what's stored in the QR Code
					labelData.setLocationName(tourLocData);

					result=null;
				//}
			}

			if (threePressed) {
				threePressed = false;
			}
		}

	}

	public void startThread() {
		if(running == false) {
			this.running = true;
			mainThread.start();
		}
	}

	public void stopThread() {
		if (running == true) {
			this.running = false;
			this.paused = false;
			int i = 0;
			while(mainThread.isAlive()) {
				System.out.println("Closing policy, please wait...");	
				i++;
				if(i > 10) {
					for (StackTraceElement ste : mainThread.getStackTrace()) {
					    System.out.println(ste);
					}
					mainThread.interrupt();
				}
				try{Thread.sleep(1000);} 
				catch (Exception e){e.printStackTrace();}
			}
		}
		joystick.StopThread();
	}

	@Override
	public void pauseThread() {
		this.paused = true;

	}

	@Override
	public void unpauseThread() {
		this.paused = false;

	}

	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof ButtonData) {
			ButtonData btn = ((ButtonData)o);
			int index = (int)arg;

			if(btn.getButtonName(index).equals("0") && 
					(btn.getButtonValue(index) > 0.0)) {
				zeroPressed = true;
			}
			if(btn.getButtonName(index).equals("1") && 
					(btn.getButtonValue(index) > 0.0)) {
				onePressed = true;
			} else {
				onePressed = false;
			}

			if (btn.getButtonName(index).equals("2")) {
				if (btn.getButtonValue(index) > 0.0) {
					twoPressed = true;
				} else {
					twoPressed = false;
				}
			}
			if(btn.getButtonName(index).equals("3") && 
					(btn.getButtonValue(index) > 0.0)) {
				threePressed = true;
			}

			if (btn.getButtonName(index).equals("Hat")) {
				//UP
				if (btn.getButtonValue(index) == 90.0) {
					upPressed = true;
				}
				else{
					upPressed = false;
				}

				//DOWN
				if (btn.getButtonValue(index) == 270.0) {
					downPressed = true;
				}
				else{
					downPressed = false;
				}

				//RIGHT
				if (btn.getButtonValue(index) == 180.0) {
					rightPressed = true;
				}
				else{
					rightPressed = false;
				}

				//LEFT
				if (btn.getButtonValue(index) == 360.0) {
					leftPressed = true;
				}
				else{
					leftPressed = false;
				}
				this.controllerUpdated();

				//90 up
				//180 right
				//270 down
				//360 left
			}

		}
	}

}
