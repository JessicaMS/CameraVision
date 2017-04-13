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
import computervision.GregggResNet;
import computervision.QRReader;
import computervision.ResNet50;
import visionPatterns.CameraProcessor;
import visionPatterns.Classifier;


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

public class StartThreaded {
	private Camera myCamera;
	private CameraProcessor cameraProcessor;
	
	public void magic() {
		//myCamera = new Kinect();
		myCamera = new LaptopCamera();

		cameraProcessor = new Classifier(new QRReader(), myCamera.getCapturedImage());
		//cameraProcessor = new Classifier(new ResNet50(), myCamera.getCapturedImage());
		//cameraProcessor = new Classifier(new GregggResNet(), myCamera.getCapturedImage());
		
		Controller myController = new TestController(cameraProcessor);
		
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
		StartThreaded funtime = new StartThreaded();
		funtime.magic();
	}
}
