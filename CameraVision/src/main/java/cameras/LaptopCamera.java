package cameras;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.WebcamListener;


public class LaptopCamera implements Camera, WebcamListener {
	private Webcam webcam;
	private JPanel cameraPanel;
	private capturedImage lastImage;

	public LaptopCamera() {
		lastImage = new capturedImage();
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
			System.out.println("Failed to initialize webcam!");
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
	
	private BufferedImage getImage() {
		return webcam.getImage();
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
		lastImage.setCameraCapture(webcamEvent.getImage());
		
	}

	@Override
	public void webcamOpen(WebcamEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	/*
	 *
	 * Return handle to lastImage, allows registration as observer
	 */
	@Override
	public capturedImage getCapturedImage() {
		
		return this.lastImage;
	}
}
