package cameras;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.WebcamListener;


public class LaptopCamera implements Camera, WebcamListener {
	private ArrayList<CameraObserver> cameraObservers;
	private Webcam webcam;
	private JPanel cameraPanel;

	public LaptopCamera() {
		cameraObservers = new ArrayList<CameraObserver>();
		initialize();
	}

	public JPanel getCameraPanel() {
		return cameraPanel;
	}


	public void setCameraPanel(JPanel cameraPanel) {
		this.cameraPanel = cameraPanel;
	}


	public Webcam getWebcam() {
		return webcam;
	}


	public void setWebcam(Webcam webcam) {
		this.webcam = webcam;
	}


	public void initialize() {
		try{
			Dimension size = WebcamResolution.QVGA.getSize();
			webcam = Webcam.getWebcams().get(0);
			if (webcam.getLock().isLocked()) {
				System.out.println("Webcam is locked");
			}
			webcam.setViewSize(size);
			cameraPanel = new WebcamPanel(webcam);
			webcam.addWebcamListener(this);
		}
		catch(Exception e){
			//IC.logError("Webcam Error", "Cannot add the webcam panel due to no webcam.", "Dashboard.java", "Dashboard()");	
		}
	}


	public void close() {
		if (webcam != null) {
			webcam.close();
		}
	}

	public JPanel getPanel() {
		return this.cameraPanel;
	}
	
	public BufferedImage getImage() {
		return webcam.getImage();
	}
//	
//	private void imageChanged() {
//		notifyWithImage();
//	}
	
//	
//	private void notifyWithImage() {
//
//	}

	public void registerCameraObserver(CameraObserver myObserver) {
		cameraObservers.add(myObserver);
	}
	
	@Override
	public void registerFPSObserver(FPSObserver myObserver) {
		// TODO Auto-generated method stub
		
	}
	
	

	@Override
	public void webcamClosed(WebcamEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void webcamDisposed(WebcamEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void webcamImageObtained(WebcamEvent webcamEvent) {
		for(CameraObserver myObserver : cameraObservers) {
			myObserver.updateBufferedImage(webcamEvent.getImage());
		}
		
	}

	@Override
	public void webcamOpen(WebcamEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
