package cameraUI;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import cameras.Camera;
import cameras.Kinect;
import cameras.LaptopCamera;
import neuralnet.SceneClassifier;


class JFrameThread implements Runnable {
	private JFrame frame;
	
	JFrameThread(JFrame frame) {
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
	private SceneClassifier myClassifier;
	private Thread classifierThread; 
	
	public void magic() {
		//myCamera = new Kinect();
		myCamera = new LaptopCamera();
		myClassifier = new SceneClassifier();
		
		CameraUI window = new CameraUI(myCamera.getPanel(), myClassifier);
		myClassifier.registerClassificationObserver(window);
		myCamera.registerCameraObserver(myClassifier);
		myCamera.registerFPSObserver(window);
		
		classifierThread = new Thread(myClassifier);
		classifierThread.start();

		JFrameThread windowThread = new JFrameThread(window.getFrame());
		
		window.getFrame().addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				myClassifier.stopThread();
				myCamera.close();
			}
		});

		EventQueue.invokeLater(windowThread);
		
	}
	
	public static void main(String[] args) {
		Start funtime = new Start();
		funtime.magic();
	}
}
