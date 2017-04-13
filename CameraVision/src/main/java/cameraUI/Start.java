package cameraUI;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cameras.Camera;
import cameras.Kinect;
import cameras.LaptopCamera;
import computervision.CameraObserver;
import computervision.Classifier;
import computervision.GregggResNet;
import computervision.QRReader;
import computervision.Recorder;
import computervision.ResNet50;
import controlPolicies.ControlPolicy;
import controlPolicies.TestController;


class JFrameThread2 implements Runnable {
	private JFrame frame;
	
	JFrameThread2(JFrame frame) {
		setJFrame(frame);
	}
	
	public void setJFrame(JFrame frame) {
		this.frame = frame;
	}
	
	public void run() {
		try {
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

public class Start {
	private Camera myCamera;
	private Classifier cameraClassifier;
	
	public void magic() {
		//myCamera = new Kinect();
		myCamera = new LaptopCamera();
		
		Recorder myRecorder = new Recorder(myCamera.getCapturedImage());

		cameraClassifier = new Classifier(new QRReader(), myCamera.getCapturedImage());
		//cameraProcessor = new Classifier(new ResNet50(), myCamera.getCapturedImage());
		//cameraProcessor = new Classifier(new GregggResNet(), myCamera.getCapturedImage());
		
		ControlPolicy myController = new TestController(cameraClassifier, myRecorder);
		
		CameraUI window = new CameraUI(myCamera.getPanel(), myController.getLabelData());
		

		JFrameThread2 windowThread = new JFrameThread2(window.getFrame());
		
		window.getFrame().addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				myController.stopThread();
				myCamera.close();
			}
		});
		
		window.getChckbxCapture().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if(window.getChckbxCapture().isSelected()) {
					myController.unpauseThread();
				} else {
					myController.pauseThread();
				}
			}
		});
		

		EventQueue.invokeLater(windowThread);
		
		myController.startThread();
		
	}
	
	public static void main(String[] args) {
		Start funtime = new Start();
		funtime.magic();
	}
}
