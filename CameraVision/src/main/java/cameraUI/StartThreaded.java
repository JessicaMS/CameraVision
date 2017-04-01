package cameraUI;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import cameras.Camera;
import cameras.LaptopCamera;
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

		cameraProcessor = new Classifier(myCamera.getCapturedImage());
		
		CameraUI window = new CameraUI(myCamera.getPanel(), cameraProcessor);
		

		JFrameThread2 windowThread = new JFrameThread2(window.getFrame());
		
		window.getFrame().addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				myCamera.close();
			}
		});

		EventQueue.invokeLater(windowThread);
		
	}
	
	public static void main(String[] args) {
		StartThreaded funtime = new StartThreaded();
		funtime.magic();
	}
}
