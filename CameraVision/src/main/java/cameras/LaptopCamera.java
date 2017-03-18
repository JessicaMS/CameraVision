package cameras;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;


public class LaptopCamera implements Camera{
	private Webcam webcam;
	private JPanel cameraPanel;

	public LaptopCamera() {
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

	@Override
	public void registerCameraObserver(CameraObserver myObserver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerFPSObserver(FPSObserver myObserver) {
		// TODO Auto-generated method stub
		
	}
}
