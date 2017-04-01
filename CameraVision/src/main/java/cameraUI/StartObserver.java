//package cameraUI;
//
//import java.awt.EventQueue;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
//
//import javax.swing.JFrame;
//import javax.swing.event.ChangeEvent;
//import javax.swing.event.ChangeListener;
//
//import cameras.Camera;
//import cameras.Kinect;
//import cameras.LaptopCamera;
//import visionPatterns.ThreadedClassifier;
//import visionPatterns.ThreadedCameraProcessor;
//
//
//class JFrameThread1 implements Runnable {
//	private JFrame frame;
//	
//	JFrameThread1(JFrame frame) {
//		setJFrame(frame);
//	}
//	
//	public void setJFrame(JFrame frame) {
//		this.frame = frame;
//	}
//	
//	public void run() {
//		try {
//			frame.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//}
//
//public class StartObserver {
//	private Camera myCamera;
//	private ThreadedCameraProcessor myImgProcessor;
//	
//	public void magic() {
//		//myCamera = new Kinect();
//		myCamera = new LaptopCamera();
//
//		myImgProcessor = new ThreadedClassifier(myCamera.getCapturedImage());
//		
//		CameraUI window = new CameraUI(myCamera.getPanel(), myImgProcessor.getImageLabel());
//		
//		myImgProcessor.startThread();
//
//		JFrameThread1 windowThread = new JFrameThread1(window.getFrame());
//		
//		window.getFrame().addWindowListener(new WindowAdapter() {
//			@Override
//			public void windowClosing(WindowEvent arg0) {
//				myImgProcessor.stopThread();
//				myCamera.close();
//			}
//		});
//		
//		window.getChckbxCapture().addChangeListener(new ChangeListener() {
//			public void stateChanged(ChangeEvent arg0) {
//				if(window.getChckbxCapture().isSelected()) {
//					myImgProcessor.setImageProcessing(true);
//				} else {
//					myImgProcessor.setImageProcessing(false);
//				}
//			}
//		});
//
//		EventQueue.invokeLater(windowThread);
//		
//	}
//	
//	public static void main(String[] args) {
//		StartObserver funtime = new StartObserver();
//		funtime.magic();
//	}
//}
